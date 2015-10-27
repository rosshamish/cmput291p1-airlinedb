package com.cmput291.rhanders_abradsha_dshin;

/**
 * Created by ross on 15-10-26.
 */
public class BookingStatus {
    private State state;
    private int ticketNo;

    // Use for an unsuccessful booking
    public BookingStatus(State state) {
        this.state = state;
        this.ticketNo = -1;
    }

    // Use for a successful booking
    public BookingStatus(int ticketNo) {
        this.state = State.SUCCESS;
        this.ticketNo = ticketNo;
    }

    public State getState() {
        return this.state;
    }

    public Boolean hasTicketNo() {
        return this.state.equals(State.SUCCESS);
    }

    public Integer getTicketNo() {
        return this.ticketNo;
    }

    public enum State {
        FAIL_NO_SEATS,
        FAIL_NO_REASON,
        SUCCESS;
    }
}
