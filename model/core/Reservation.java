package model.core;

import java.time.Instant;
import java.util.Objects;

import model.enums.ReservationStatus;

/**
 * Base abstraction for any reservable entity (e.g., Ticket).
 * <p>
 * <b>Invariants and Lifecycle:</b>
 * <ul>
 *   <li><b>reservationId</b> is globally unique and immutable for the lifetime of the reservation.</li>
 *   <li><b>Status transitions</b> are strictly controlled:
 *     <ul>
 *       <li>PENDING → CONFIRMED → CANCELED</li>
 *       <li>PENDING → CANCELED</li>
 *     </ul>
 *     No other transitions are allowed.</li>
 *   <li><b>Equality</b> is defined by reservationId across all subclasses.</li>
 *   <li>Subclasses must implement cancellation logic and may override penalty computation.</li>
 *   <li>Persistence and consumers should rely on reservationId as the stable identifier.</li>
 * </ul>
 * Provides id/status/creation time and guarded state transitions.
 */
public abstract class Reservation {
    private final String reservationId;
    private final Instant createdAt;
    private ReservationStatus status;

    /** Convenience constructor: defaults to now + PENDING. */
    protected Reservation(String reservationId) {
        this(reservationId, Instant.now(), ReservationStatus.PENDING);
    }

    public Reservation(String reservationId, Instant createdAt, ReservationStatus status) {
        this.reservationId = Objects.requireNonNull(reservationId, "reservationId");
        this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
        this.status = Objects.requireNonNull(status, "status");
    }

    public String getReservationId() { return reservationId; }
    public Instant getCreatedAt() { return createdAt; }
    public ReservationStatus getStatus() { return status; }

    /** Confirm only from PENDING; throws otherwise. */
    public final void confirm() {
        ensureStatus(ReservationStatus.PENDING, "Only PENDING reservations can be confirmed");
        this.status = ReservationStatus.CONFIRMED;
        onStatusChanged(ReservationStatus.CONFIRMED);
    }

    public boolean isActive() {
        return status == ReservationStatus.CONFIRMED;
    }

    /**
     * Subclasses must implement how to cancel (release resources, compute/charge penalty, etc.).
     * Typical flow in subclass:
     *  - validate current status
     *  - double penalty = computePenalty(cancelTime)
     *  - apply refund/charge and release resources
     *  - call markCanceled()
     */
    public abstract void cancel(Instant cancelTime);

    /** Default: no penalty. Subclasses may override. */
    public double computePenalty(Instant cancelTime) {
        return 0.0;
    }

    /** Helper for subclasses to finalize cancellation status. */
    protected final void markCanceled() {
        if (status == ReservationStatus.CANCELED) return;
        this.status = ReservationStatus.CANCELED;
        onStatusChanged(ReservationStatus.CANCELED);
    }

    /** Optional hook for subclasses (e.g., emit events); no-op by default. */
    protected void onStatusChanged(ReservationStatus newStatus) { /* no-op */ }

    /** Guard helper to ensure current status equals expected. */
    protected final void ensureStatus(ReservationStatus expected, String message) {
        if (this.status != expected) {
            throw new IllegalStateException(message + " (current=" + this.status + ")");
        }
    }

    @Override public String toString() {
        return "Reservation{id=" + reservationId + ", status=" + status + ", createdAt=" + createdAt + "}";
    }

    /** Equality by stable id is usually enough. */
    @Override public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Reservation)) return false;
        Reservation other = (Reservation) o;
        return reservationId.equals(other.reservationId);
    }

    @Override public final int hashCode() {
        return reservationId.hashCode();
    }
}
