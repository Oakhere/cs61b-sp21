package gitlet;

import java.io.*;
import java.util.*;

import static gitlet.Utils.*;

// TODO: any imports you need here

/** Represents a gitlet repository.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Repository {
    /**
     * TODO: add instance variables here.
     *
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** A mapping from branch heads to references to commits(actually their SHA-1 code),
     *  so that certain important commits have symbolic names.*/
    public static HashMap<String, String> branches;
    /** Every time we make a new commit, add it's SHA-1 to the commit tree*/
    public static HashSet<String> commitTree;
    public static StagingArea stagingArea;

    /** A file to store the commit tree to disk. */
    public static final File commitTreeText = join(GITLET_DIR, "commitTree.txt");
    public static final File branchesText = join(GITLET_DIR, "branches.txt");
    public static final File stagingAreaText = join(GITLET_DIR, "stagingArea.txt");

    private static void setUpPersistence() throws IOException {
        GITLET_DIR.mkdir();
        commitTreeText.createNewFile();
        branchesText.createNewFile();
        stagingAreaText.createNewFile();
        stagingArea = new StagingArea();
        writeObject(stagingAreaText, stagingArea);
    }

    /** Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit that contains
     * no files and has the commit message initial commit. It will have a single
     * branch: master, which initially points to this initial commit, and master
     * will be the current branch. */
    public static void init() throws IOException {
        // failure cases
        if (GITLET_DIR.exists()) {
            message("A Gitlet version-control system already " +
                    "exists in the current directory.");
            System.exit(0);
        }
        // create the commit tree, the branches map, and the initial commit
        setUpPersistence();
        commitTree = new HashSet<>();
        branches = new HashMap<>();
        Commit initial = new Commit("initial commit", "");
        commitTree.add(sha1(serialize(initial)));
        branches.put("master", sha1(serialize(initial)));
        // The HEAD pointer keeps track of where in the linked list we currently are.
        branches.put("HEAD", sha1(serialize(initial)));
        // set persistence
        initial.saveCommit();
        writeObject(commitTreeText, commitTree);
        writeObject(branchesText, branches);
    }

    /** Adds a copy of the file as it currently exists to the staging area. */
    public static void add(String fileName) throws IOException {
        File f = join(CWD, fileName);
        if (!f.exists()) {
            message("File does not exist.");
            System.exit(0);
        }
        stagingArea = readObject(stagingAreaText, StagingArea.class);
        stagingArea.add(f);
        writeObject(stagingAreaText, stagingArea);
    }

    /** Saves a snapshot of tracked files in the current commit and staging area so
     * that they can be restored at a later time, creating a new commit.*/
    public static void commit(String message) throws IOException {
        stagingArea = readObject(stagingAreaText, StagingArea.class);
        // failure cases
        if (stagingArea.blobsForAddition.isEmpty() && stagingArea.blobsForRemoval.isEmpty()) {
            message("No changes added to the commit.");
            System.exit(0);
        }
        if (message.isEmpty()) {
            message("Please enter a commit message.");
            System.exit(0);
        }
        // find the parent commit, create a new commit
        commitTree = readObject(commitTreeText, HashSet.class);
        branches = readObject(branchesText, HashMap.class);
        Commit parent = Commit.getCommit(branches.get("HEAD"));
        Commit newCommit = new Commit(message, sha1(serialize(parent)));
        // inherit its parent's blobs
        newCommit.blobs.putAll(parent.blobs);
        // update the file in the staging area to the commit
        newCommit.blobs.putAll(stagingArea.blobsForAddition);
        for (String f : stagingArea.blobsForRemoval) {
            newCommit.blobs.remove(f);
        }
        // clear the staging area
        stagingArea.blobsForAddition.clear();
        stagingArea.blobsForRemoval.clear();
        writeObject(stagingAreaText, stagingArea);
        // add the commit to the commit tree, change the pointers
        commitTree.add(sha1(serialize(newCommit)));
        branches.put("HEAD", sha1(serialize(newCommit)));
        branches.put("master", sha1(serialize(newCommit)));
        // set persistence
        newCommit.saveCommit();
        writeObject(commitTreeText, commitTree);
        writeObject(branchesText, branches);
    }

    /** Unstage the file if it is currently staged for addition. If the file is
     * tracked in the current commit, stage it for removal and remove the file from
     * the working directory if the user has not already done so (do not remove it
     * unless it is tracked in the current commit).*/
    public static void rm(String filename) {
        stagingArea = readObject(stagingAreaText, StagingArea.class);
        branches = readObject(branchesText, HashMap.class);
        Commit head = Commit.getCommit(branches.get("HEAD"));
        // failure cases
        if (!stagingArea.blobsForAddition.containsKey(filename) &&
                !head.blobs.containsKey(filename)) {
            message("No reason to remove the file.");
            System.exit(0);
        }
        // make sure the file is no longer staged for addition
        stagingArea.blobsForAddition.remove(filename);
        // if the file is tracked in the current commit, stage it for removal and
        // remove the file from the working directory if the user has not already done so
        if (head.blobs.containsKey(filename)) {
            restrictedDelete(filename);
            stagingArea.blobsForRemoval.add(filename);
        }
        writeObject(stagingAreaText, stagingArea);
    }

    /**  Starting at the current head commit, display information about each commit
     * backwards along the commit tree until the initial commit, following the first
     * parent commit links, ignoring any second parents found in merge commits.*/
    public static void log() {
        branches = readObject(branchesText, HashMap.class);
        commitTree = readObject(commitTreeText, HashSet.class);
        Commit current = Commit.getCommit(branches.get("HEAD"));
        while (current != null) {
            current.printLog();
            current = Commit.getCommit(current.getParent());
        }
        // need to handle merge commits
    }

    /** Like log, except displays information about all commits ever made.
     * The order of the commits does not matter. */
    public static void globalLog() {
        commitTree = readObject(commitTreeText, HashSet.class);
        for (String sha1 : commitTree) {
            Commit current = Commit.getCommit(sha1);
            current.printLog();
        }
    }

    /**  Prints out the ids of all commits that have the given commit message. */
    public static void find(String message) {
        commitTree = readObject(commitTreeText, HashSet.class);
        boolean commitFound = false;
        for (String sha1 : commitTree) {
            Commit current = Commit.getCommit(sha1);
            if (current.getMessage().equals(message)) {
                System.out.println(sha1);
                commitFound = true;
            }
        }
        if (!commitFound) {
            message("Found no commit with that message.");
            System.exit(0);
        }
    }

    /** Displays what branches currently exist, and marks the current branch with a *.
     * Also displays what files have been staged for addition or removal. */
    public static void status() {
        stagingArea = readObject(stagingAreaText, StagingArea.class);
        branches = readObject(branchesText, HashMap.class);
        System.out.println("=== Branches ===");
        String head = branches.get("HEAD");
        List<String> keysList = new ArrayList<>(branches.keySet());
        Collections.sort(keysList);
        for (String key : keysList) {
            // skip the HEAD since it's just a pointer, not exactly a branch
            if (key.equals("HEAD")) {
                continue;
            }
            // the current branch gets an extra *
            if (branches.get(key).equals(head)) {
                System.out.println("*" + key);
            } else {
                System.out.println(key);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        List<String> addList = new ArrayList<>(stagingArea.blobsForAddition.keySet());
        Collections.sort(addList);
        for (String f : addList) {
            System.out.println(f);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        List<String> removeList = new ArrayList<>(stagingArea.blobsForRemoval);
        Collections.sort(removeList);
        for (String f : removeList) {
            System.out.println(f);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        // implement for extra credit
        System.out.println();
        System.out.println("=== Untracked Files ===");
        // implement for extra credit
        System.out.println();
    }

    /** Creates a new branch with the given name, and points it at the current head commit.
     * Noted that a branch is nothing more than a name for a reference (a SHA-1 identifier) to
     * a commit node. */
    public static void branch(String branchName) {
        branches = readObject(branchesText, HashMap.class);
        // failure cases
        if (branches.containsKey(branchName)) {
            message("A branch with that name already exists.");
            System.exit(0);
        }
        // find the current head commit, create a new branch and points it at the current head commit.
        String head = branches.get("HEAD");
        branches.put(branchName, head);
    }


}
