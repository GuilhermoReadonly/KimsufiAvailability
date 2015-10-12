package com.readonly.kimsufiavailability;

public final class Constants {

    // Defines a custom Intent action
    public static final String BROADCAST_ACTION = "com.readonly.kimsufiavailability.BROADCAST";

    // Defines the key for the status "extra" in an Intent
    public static final String EXTENDED_DATA_STATUS = "com.readonly.kimsufiavailability.STATUS";


    // Defines the start text for the start/stop button "extra"
    public static final String UI_BUTTON_START = "Start service !";

    // Defines the stop text for the start/stop button "extra"
    public static final String UI_BUTTON_STOP = "Stop service !";

    public static final Server SERVER_KS1 = new Server("KS-1","150sk10",174);
    public static final Server SERVER_KS2 = new Server("KS-2","150sk20",175);
    public static final Server SERVER_KS2SSD = new Server("KS-2SSD","150sk22",176);
    public static final Server SERVER_KS3 = new Server("KS-3","150sk30",177);
    public static final Server SERVER_KS4 = new Server("KS-4","150sk40",178);
    public static final Server SERVER_KS5 = new Server("KS-5","150sk50",181);
    public static final Server SERVER_KS6 = new Server("KS-6","150sk60",182);

}

