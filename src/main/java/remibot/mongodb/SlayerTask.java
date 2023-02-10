package remibot.mongodb;

public class SlayerTask {

    private String taskName;
    private int amount;
    private int currentKC;

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCurrentKC() {
        return currentKC;
    }

    public void setCurrentKC(int currentKC) {
        this.currentKC = currentKC;
    }

    @Override
    public String toString() {
        return "SlayerTask{" +
                "task='" + taskName + '\'' +
                ", amount=" + amount +
                ", currentKC=" + currentKC +
                '}';
    }
}
