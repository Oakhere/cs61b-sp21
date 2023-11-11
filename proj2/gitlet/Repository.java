package gitlet;

import java.io.*;
import java.util.HashMap;
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

    /** A mapping from branch heads to references to commits, so that certain
     * important commits have symbolic names.*/
    public static HashMap<String, Commit> branches;
    /** Every time we make a new commit, add it to the commit tree, mapping its SHA1
     * to the commit object. */
    public static HashMap<String, Commit> commitTree;
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
        if (GITLET_DIR.exists()) {
            message("A Gitlet version-control system already " +
                    "exists in the current directory.");
            System.exit(0);
        }
        setUpPersistence();
        commitTree = new HashMap<>();
        branches = new HashMap<>();
        Commit initial = new Commit("initial commit", "");
        commitTree.put(sha1(serialize(initial)), initial);
        branches.put("master", initial);
        // The HEAD pointer keeps track of where in the linked list we currently are.
        branches.put("HEAD", initial);
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
    public static void commit(String message) {
        stagingArea = readObject(stagingAreaText, StagingArea.class);
        // failure cases
        if (stagingArea.blobsForAddition.isEmpty()) {
            message("No changes added to the commit.");
            System.exit(0);
        }
        if (message.isEmpty()) {
            message("Please enter a commit message.");
            System.exit(0);
        }

        commitTree = readObject(commitTreeText, HashMap.class);
        branches = readObject(branchesText, HashMap.class);
        Commit parent = branches.get("HEAD");
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
        commitTree.put(sha1(serialize(newCommit)), newCommit);
        branches.put("HEAD", newCommit);
        branches.put("master", newCommit);
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
        Commit head = branches.get("HEAD");
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
    }
}
