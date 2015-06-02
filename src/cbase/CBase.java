package cbase;

import java.util.Arrays;

/**
 * @author obyte
 */
public class CBase {

    public static void main(String[] args) {
        ContactCommander cc = new ContactCommander();
        if (args.length == 0) {
            cc.getCommand();
        } else {
            cc.commandParser(StringParser.parseCommandLine(Arrays.toString(args)));
            cc.getCommand();
        }
    }
}
