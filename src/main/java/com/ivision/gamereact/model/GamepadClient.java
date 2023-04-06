package ivisionfx.interaction.gamepad;

import TUIO.TuioClient;

public class GamepadClient  extends TuioClient {

    public void connect() {
        if(!isConnected()) super.connect();
    }
}
