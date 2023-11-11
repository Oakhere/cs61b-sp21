package gitlet;

import java.io.IOException;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        // TODO: what if args is empty?
        if (args.length == 0) {
            Utils.message("Please enter a command.");
            return;
        }
        String firstArg = args[0];
        switch (firstArg) {
            case "init" -> Repository.init();
            case "add" -> {
                String fileName = args[1];
                Repository.add(fileName);
            }
            case "commit" -> {
                String message = args[1];
                Repository.commit(message);
            }
            case "rm" -> {
                String filename = args[1];
                Repository.rm(filename);
            }
            case "log" -> Repository.log();
            case "find" -> {
                String message = args[1];
                Repository.find(message);
            }
            case "global-log" -> Repository.globalLog();
            case "status" -> Repository.status();
            case "branch" -> {
                String branchName = args[1];
                Repository.branch(branchName);
            }
        }
    }
}
