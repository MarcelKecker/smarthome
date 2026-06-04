package model.scenario;

import model.ActionType;
import model.action.Command;
import model.device.Device;

// Einfacher Stub/Mock für die Testfälle
class TestCommand implements Command {
    private final String id;
    private boolean executed = false;
    private int orderIndex = 0;

    public TestCommand(String id) {
        this.id = id;
    }

    @Override
    public void execute() {
        this.executed = true;
    }

    @Override
    public String getID() {
        return id;
    }

    public boolean isExecuted() {
        return executed;
    }

    @Override
    public Device getDevice() {
        return null;
    }

    @Override
    public ActionType getActionType() {
        return null;
    }

    @Override
    public int getOrderIndex() {
        return orderIndex;
    }

    @Override
    public void setOrderIndex(int i) {
        this.orderIndex = i;
    }

    @Override
    public String toString() {
        return "TestCommand[" + id + "]";
    }
}