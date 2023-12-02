package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
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
            case "checkout" -> {
                if (args.length == 2) {
                    String branchName = args[1];
                    Repository.checkoutBranch(branchName);
                } else if (args.length == 3 && args[1].equals("--")) {
                    String fileName = args[2];
                    Repository.checkoutFile(fileName);
                } else if (args.length == 4 && args[2].equals("--")) {
                    String commitID = args[1];
                    String fileName = args[3];
                    Repository.checkoutCommitFile(commitID, fileName);
                } else {
                    Utils.message("Invalid checkout command.");
                }
            }
            case "branch" -> {
                String branchName = args[1];
                Repository.branch(branchName);
            }
            case "rm-branch" -> {
                String branchName = args[1];
                Repository.removeBranch(branchName);
            }
            case "reset" -> {
                String commitID = args[1];
                Repository.reset(commitID);
            }
            case "merge" -> {
                String branchName = args[1];
                Repository.merge(branchName);
            }
        }
    }
}
