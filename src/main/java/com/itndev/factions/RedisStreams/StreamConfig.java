package com.itndev.factions.RedisStreams;

public class StreamConfig {

    private static String Stream_INPUT_NAME = "INPUT_FRONTEND_TO_BACKEND";
    private static String Stream_OUTPUT_NAME = "OUTPUT_BACKEND_TO_FRONTEND";
    private static String Stream_INTERCONNECT_NAME = "INTERCONNECT_INNER";
    private static String Stream_BUNGEE_PIPELINE_NAME = "BUNGEE_PIPELINE";
    private static String Stream_INTERCONNECT2_NAME = "INTERCONNECT_INNER2";

    public static String get_Stream_INPUT_NAME() {
        return Stream_INPUT_NAME;
    }

    public static String get_Stream_OUTPUT_NAME() {
        return Stream_OUTPUT_NAME;
    }

    public static String get_Stream_INTERCONNECT_NAME() {
        return Stream_INTERCONNECT_NAME;
    }

    public static String get_Stream_BUNGEE_LINE() {
        return Stream_BUNGEE_PIPELINE_NAME;
    }

    public static String get_Stream_INTERCONNECT2_NAME() {
        return Stream_INTERCONNECT2_NAME;
    }
}
