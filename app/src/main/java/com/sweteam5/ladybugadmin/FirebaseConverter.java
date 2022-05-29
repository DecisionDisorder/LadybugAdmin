package com.sweteam5.ladybugadmin;

public class FirebaseConverter {

    public static boolean[] convertString2BoolList(String boolArray) {
        String subStr = boolArray.substring(boolArray.indexOf(',') + 2);
        subStr = subStr.replaceAll("]", "");
        subStr = subStr.replaceAll(" ", "");
        String[] res = subStr.split(",");
        boolean[] result = new boolean[res.length];
        for (int i = 0; i < result.length; i++)
            result[i] = Boolean.parseBoolean(res[i]);

        return result;
    }

    public static String[] convertDict2CodeList(String codeInDictionary) {
        String[] list = codeInDictionary.split(",");
        for(int i = 0; i < list.length; i++) {
            list[i] = list[i].replaceAll("[^0-9]", "");
        }
        return list;
    }

    public static int[] convertMsg2BusLocation(String message) {
        int[] result = new int[3];
        message = message.trim();
        int index = Integer.parseInt(message.substring(message.indexOf('_') + 1, message.indexOf('_') + 2)) - 1;
        result[index] = Integer.parseInt(message.substring(message.indexOf('=') + 1, message.indexOf(',')));
        message = message.substring(message.indexOf(',') + 1);

        index = Integer.parseInt(message.substring(message.indexOf('_') + 1, message.indexOf('_') + 2)) - 1;
        result[index] = Integer.parseInt(message.substring(message.indexOf('=') + 1, message.indexOf(',')));
        message = message.substring(message.indexOf(',') + 1);

        index = Integer.parseInt(message.substring(message.indexOf('_') + 1, message.indexOf('_') + 2)) - 1;
        result[index] = Integer.parseInt(message.substring(message.indexOf('=') + 1, message.indexOf('}')));

        return result;
    }
}
