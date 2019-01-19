package busime.cm.catchussd.security;

public class PermissionManager {

    private boolean canCallPhone = false;

    private boolean canReadPhoneState = false;

    public boolean isCanCallPhone() {
        return canCallPhone;
    }

    public void setCanCallPhone(boolean canCallPhone) {
        this.canCallPhone = canCallPhone;
    }

    public boolean isCanReadPhoneState() {
        return canReadPhoneState;
    }

    public void setCanReadPhoneState(boolean canReadPhoneState) {
        this.canReadPhoneState = canReadPhoneState;
    }
}
