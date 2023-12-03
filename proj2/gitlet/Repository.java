package gitlet;

import java.io.*;
import java.util.*;

import static gitlet.Utils.*;

/** Represents a gitlet repository. */
public class Repository {
    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /** A mapping from branch heads to references to commits(actually their SHA-1 code),
     *  so that certain important commits have symbolic names. Exception: "HEAD" maps to
     *  the name of the current branch, e.g. "HEAD" -> "master". */
    static HashMap<String, String> branches;
    /** Every time we make a new commit, add it's SHA-1 to the commit tree*/
    static HashSet<String> commitTree;
    static StagingArea stagingArea;

    /** A file to store the commit tree to disk. */
    public static final File COMMIT_TREE_TEXT = join(GITLET_DIR, "commitTree.txt");
    public static final File BRANCHES_TEXT = join(GITLET_DIR, "branches.txt");
    public static final File STAGING_AREA_TEXT = join(GITLET_DIR, "stagingArea.txt");

    private static void setUpPersistence() {
        GITLET_DIR.mkdir();
        stagingArea = new StagingArea();
        writeObject(STAGING_AREA_TEXT, stagingArea);
    }

    /** Creates a new Gitlet version-control system in the current directory.
     * This system will automatically start with one commit: a commit that contains
     * no files and has the commit message initial commit. It will have a single
     * branch: master, which initially points to this initial commit, and master
     * will be the current branch. */
    public static void init() {
        // failure cases
        if (GITLET_DIR.exists()) {
            message("A Gitlet version-control system already "
                    + "exists in the current directory.");
            System.exit(0);
        }
        // create the commit tree, the branches map, and the initial commit
        setUpPersistence();
        commitTree = new HashSet<>();
        branches = new HashMap<>();
        Commit initial = new Commit("initial commit", "");
        String initialSha1 = sha1(serialize(initial));
        commitTree.add(initialSha1);
        branches.put("master", initialSha1);
        // The HEAD pointer keeps track of where in the linked list we currently are.
        branches.put("HEAD", "master");
        // set persistence
        initial.saveCommit(initialSha1);
        writeObject(COMMIT_TREE_TEXT, commitTree);
        writeObject(BRANCHES_TEXT, branches);
    }

    /** Adds a copy of the file as it currently exists to the staging area. */
    public static void add(String fileName) {
        File f = join(CWD, fileName);
        String filename = f.getName();
        if (!f.exists()) {
            message("File does not exist.");
            System.exit(0);
        }
        stagingArea = readObject(STAGING_AREA_TEXT, StagingArea.class);
        branches = readObject(BRANCHES_TEXT, HashMap.class);

        // adding a tracked, unchanged file has no effect.
        Commit head = Commit.getCommit(branches.get(branches.get("HEAD")));
        if (head.blobs.containsKey(filename) && !stagingArea.blobsForRemoval.contains(filename)) {
            Blob b = Blob.getBlob(head.blobs.get(f.getName()));
            if (b.contents.equals(readContentsAsString(f))) {
                return;
            }
        }

        stagingArea.add(f);
        writeObject(STAGING_AREA_TEXT, stagingArea);
    }

    /** Saves a snapshot of tracked files in the current commit and staging area so
     * that they can be restored at a later time, creating a new commit.*/
    public static void commit(String message, String secondParent) {
        stagingArea = readObject(STAGING_AREA_TEXT, StagingArea.class);
        // failure cases
        if (stagingArea.isEmpty()) {
            message("No changes added to the commit.");
            System.exit(0);
        }
        if (message.isEmpty()) {
            message("Please enter a commit message.");
            System.exit(0);
        }
        // find the parent commit, create a new commit
        commitTree = readObject(COMMIT_TREE_TEXT, HashSet.class);
        branches = readObject(BRANCHES_TEXT, HashMap.class);
        Commit parent = Commit.getCommit(branches.get(branches.get("HEAD")));
        Commit newCommit;
        if (secondParent.isEmpty()) {
            newCommit = new Commit(message, branches.get(branches.get("HEAD")));
        } else {
            newCommit = new Commit(message, branches.get(branches.get("HEAD")), secondParent);
        }
        // inherit its parent's blobs
        newCommit.blobs.putAll(parent.blobs);
        // update the file in the staging area to the commit
        newCommit.blobs.putAll(stagingArea.blobsForAddition);
        for (String f : stagingArea.blobsForRemoval) {
            newCommit.blobs.remove(f);
        }
        // clear the staging area
        stagingArea.clear();
        writeObject(STAGING_AREA_TEXT, stagingArea);
        // add the commit to the commit tree, change the pointers
        String newCommitSha1 = sha1(serialize(newCommit));
        commitTree.add(newCommitSha1);
        branches.put(branches.get("HEAD"), newCommitSha1);
        // set persistence
        newCommit.saveCommit(newCommitSha1);
        writeObject(COMMIT_TREE_TEXT, commitTree);
        writeObject(BRANCHES_TEXT, branches);
    }

    public static void commit(String message) {
        String secondParent = "";
        commit(message, secondParent);
    }

    /** Unstage the file if it is currently staged for addition. If the file is
     * tracked in the current commit, stage it for removal and remove the file from
     * the working directory if the user has not already done so (do not remove it
     * unless it is tracked in the current commit).*/
    public static void rm(String filename) {
        stagingArea = readObject(STAGING_AREA_TEXT, StagingArea.class);
        branches = readObject(BRANCHES_TEXT, HashMap.class);
        Commit head = Commit.getCommit(branches.get(branches.get("HEAD")));
        // failure cases
        if (!stagingArea.blobsForAddition.containsKey(filename)
                && !head.blobs.containsKey(filename)) {
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
        writeObject(STAGING_AREA_TEXT, stagingArea);
    }

    /**  Starting at the current head commit, display information about each commit
     * backwards along the commit tree until the initial commit, following the first
     * parent commit links, ignoring any second parents found in merge commits.*/
    public static void log() {
        branches = readObject(BRANCHES_TEXT, HashMap.class);
        commitTree = readObject(COMMIT_TREE_TEXT, HashSet.class);
        Commit current = Commit.getCommit(branches.get(branches.get("HEAD")));
        while (current != null) {
            current.printLog();
            current = Commit.getCommit(current.getParent());
        }
        // need to handle merge commits
    }

    /** Like log, except displays information about all commits ever made.
     * The order of the commits does not matter. */
    public static void globalLog() {
        commitTree = readObject(COMMIT_TREE_TEXT, HashSet.class);
        for (String sha1 : commitTree) {
            Commit current = Commit.getCommit(sha1);
            current.printLog();
        }
    }

    /**  Prints out the ids of all commits that have the given commit message. */
    public static void find(String message) {
        commitTree = readObject(COMMIT_TREE_TEXT, HashSet.class);
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
        stagingArea = readObject(STAGING_AREA_TEXT, StagingArea.class);
        branches = readObject(BRANCHES_TEXT, HashMap.class);
        System.out.println("=== Branches ===");
        List<String> keysList = new ArrayList<>(branches.keySet());
        Collections.sort(keysList);
        for (String key : keysList) {
            // skip the HEAD since it's just a pointer, not exactly a branch
            if (key.equals("HEAD")) {
                continue;
            }
            // the current branch gets an extra *
            if (key.equals(branches.get("HEAD"))) {
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
        branches = readObject(BRANCHES_TEXT, HashMap.class);
        // failure cases
        if (branches.containsKey(branchName)) {
            message("A branch with that name already exists.");
            System.exit(0);
        }
        // find HEAD(current head commit), create a new branch and points it at the HEAD.
        String headSha1 = branches.get(branches.get("HEAD"));
        branches.put(branchName, headSha1);
        writeObject(BRANCHES_TEXT, branches);
    }

    /** Takes the version of the file as it exists in the head commit and puts it in the working
     * directory, overwriting the version of the file that’s already there if there is one.
     * The new version of the file is not staged.*/
    public static void checkoutFile(String fileName) {
        branches = readObject(BRANCHES_TEXT, HashMap.class);
        Commit head = Commit.getCommit(branches.get(branches.get("HEAD")));
        // failure cases
        if (!head.blobs.containsKey(fileName)) {
            message("File does not exist in that commit.");
            System.exit(0);
        }
        // create or rewrite file
        File localFile = join(CWD, fileName);
        writeContents(localFile, Blob.getBlob(head.blobs.get(fileName)).contents);
    }
    /** Takes the version of the file as it exists in the commit with the given id, and puts it
     * in the working directory, overwriting the version of the file that’s already there if
     * there is one. The new version of the file is not staged.*/
    public static void checkoutCommitFile(String commitID, String fileName) {
        commitID = fullCommitID(commitID);
        // if such id doesn't exit, fullCommitID returns an empty string
        if (commitID.isEmpty()) {
            message("No commit with that id exists.");
            System.exit(0);
        }
        Commit givenCommit = Commit.getCommit(commitID);
        if (!givenCommit.blobs.containsKey(fileName)) {
            message("File does not exist in that commit.");
            System.exit(0);
        }
        // create or rewrite file
        File localFile = join(CWD, fileName);
        writeContents(localFile, Blob.getBlob(givenCommit.blobs.get(fileName)).contents);
    }

    /** A helper method for finding the full commit id using its prefix. If such id doesn't exist,
     * return an empty string. */
    private static String fullCommitID(String prefix) {
        commitTree = readObject(COMMIT_TREE_TEXT, HashSet.class);
        for (String id : commitTree) {
            if (id.startsWith(prefix)) {
                return id;
            }
        }
        return "";
    }
    /** Takes all files in the commit at the head of the given branch, and puts them in the
     * working directory, overwriting the versions of the files that are already there if they
     * exist. Also, at the end of this command, the given branch will now be considered the
     * current branch (HEAD). Any files that are tracked in the current branch but are not present
     * in the checked-out branch are deleted. The staging area is cleared, unless the checked-out
     * branch is the current branch(failure case). */
    public static void checkoutBranch(String branchName) {
        branches = readObject(BRANCHES_TEXT, HashMap.class);
        // failure cases
        if (!branches.containsKey(branchName)) {
            message("No such branch exists.");
            System.exit(0);
        }
        if (branchName.equals(branches.get("HEAD"))) {
            message("No need to checkout the current branch.");
            System.exit(0);
        }
        Commit checkoutCommit = Commit.getCommit(branches.get(branchName));
        Commit currentCommit = Commit.getCommit(branches.get(branches.get("HEAD")));
        Set<String> fileInCurrentCommit = currentCommit.blobs.keySet();
        Set<String> fileInCheckoutCommit = checkoutCommit.blobs.keySet();
        for (String f : plainFilenamesIn(CWD)) {
            if (!f.startsWith(".") && !fileInCurrentCommit.contains(f)
                    && fileInCheckoutCommit.contains(f)) {
                message("There is an untracked file in the way; delete it, or add and "
                        + "commit it first.");
                System.exit(0);
            }
        }
        // iterate through all the files tracked by the current commit. if they don't exist in
        // the checkout commit, delete them.
        for (String f : fileInCurrentCommit) {
            if (!fileInCheckoutCommit.contains(f)) {
                restrictedDelete(join(CWD, f));
            }
        }
        // iterate through all the files tracked by the checkout commit, create or overwrite them
        for (String f : fileInCheckoutCommit) {
            File file = join(CWD, f);
            writeContents(file, Blob.getBlob(checkoutCommit.blobs.get(f)).contents);
        }
        // set the checkout branch as the current branch (HEAD)
        branches.put("HEAD", branchName);
        writeObject(BRANCHES_TEXT, branches);
        // clear the staging area
        stagingArea = readObject(STAGING_AREA_TEXT, StagingArea.class);
        stagingArea.clear();
        writeObject(STAGING_AREA_TEXT, stagingArea);
    }

    /** Deletes the branch(pointer) with the given name.*/
    public static void removeBranch(String branchName) {
        branches = readObject(BRANCHES_TEXT, HashMap.class);
        // failure cases
        if (!branches.containsKey(branchName)) {
            message("A branch with that name does not exist.");
            System.exit(0);
        }
        if (branches.get("HEAD").equals(branchName)) {
            message("Cannot remove the current branch.");
            System.exit(0);
        }
        // remove the branch name
        branches.remove(branchName);
        writeObject(BRANCHES_TEXT, branches);
    }

    /** Checks out all the files tracked by the given commit. Removes tracked files
     * that are not present in that commit. Also moves the current branch’s head to
     * that commit node. The staging area is cleared. */
    public static void reset(String commitID) {
        branches = readObject(BRANCHES_TEXT, HashMap.class);
        commitID = fullCommitID(commitID);
        if (commitID.isEmpty()) {
            message("No commit with that id exists.");
            System.exit(0);
        }
        String currentBranchName = branches.get("HEAD");
        branches.put("temp", commitID);
        writeObject(BRANCHES_TEXT, branches);
        checkoutBranch("temp");
        branches.remove("temp");
        branches.put(currentBranchName, commitID);
        branches.put("HEAD", currentBranchName);
        writeObject(BRANCHES_TEXT, branches);
        // temporarily create a branch called "temp" in branches so that we can call the
        // previous checkoutBranch method.
        //branches.put(commitID, fullCommitID);
        //writeObject(BRANCHES_TEXT, branches);
        //checkoutBranch(commitID);

    }

    /** Merges files from the given branch("other") into the current branch("head"). */
    public static void merge(String branchName) {
        boolean conflictEncountered = false;
        stagingArea = readObject(STAGING_AREA_TEXT, StagingArea.class);
        // failure case 1
        if (!stagingArea.isEmpty()) {
            message("You have uncommitted changes.");
            System.exit(0);
        }

        branches = readObject(BRANCHES_TEXT, HashMap.class);
        // failure case 2
        if (!branches.containsKey(branchName)) {
            message("A branch with that name does not exist.");
            System.exit(0);
        }
        // failure case 3
        if (branches.get("HEAD").equals(branchName)) {
            message("Cannot merge a branch with itself.");
            System.exit(0);
        }

        Commit head = Commit.getCommit(branches.get(branches.get("HEAD")));
        Commit other = Commit.getCommit(branches.get(branchName));
        // failure case 4
        Set<String> fileInHead = head.blobs.keySet();
        Set<String> fileInOther = other.blobs.keySet();
        for (String f : plainFilenamesIn(CWD)) {
            // If an untracked file in the current commit would be overwritten or deleted by merge
            if (!f.startsWith(".") && !fileInHead.contains(f) && fileInOther.contains(f)) {
                message("There is an untracked file in the way; delete it, or add and "
                        + "commit it first.");
                System.exit(0);
            }
        }

        Commit split = findSplitPoint(head, other);
        if (split.equals(other)) {
            message("Given branch is an ancestor of the current branch.");
            return;
        }
        if (split.equals(head)) {
            checkoutBranch(branchName);
            message("Current branch fast-forwarded.");
            return;
        }

        // real merge happens:
        // put all file names(whether in split point, current branch or given branch) into this set
        HashSet<String> allFiles = new HashSet<>();
        allFiles.addAll(split.blobs.keySet());
        allFiles.addAll(head.blobs.keySet());
        allFiles.addAll(other.blobs.keySet());
        // iterate through all the files, perform different operations accordingly
        for (String f : allFiles) {
            // since blobs are content-addressable, use SHA-1 to determine whether they're modified
            String inHead = head.blobs.get(f);
            String inOther = other.blobs.get(f);
            String inSplit = split.blobs.get(f);
            if (inHead == null) {
                inHead = "";
            }
            if (inOther == null) {
                inOther = "";
            }
            if (inSplit == null) {
                inSplit = "";
            }

            // modified in other but not in HEAD
            if (!inOther.equals(inSplit) && inHead.equals(inSplit)) {
                if (inOther.isEmpty()) {
                    rm(f);
                } else {
                    checkoutCommitFile(other.getSha1(), f);
                    add(f);
                }
            // modified in both other and in HEAD, and in different ways, i.e. conflict
            } else if (!inOther.equals(inSplit) && !inOther.equals(inHead)) {
                conflictEncountered = true;
                File conflictFile = join(CWD, f);
                String contentInHead, contentInOther;
                if (inHead.isEmpty()) {
                    contentInHead = "";
                } else {
                    contentInHead = Blob.getBlob(head.blobs.get(f)).contents;
                }
                if (inOther.isEmpty()) {
                    contentInOther = "";
                } else {
                    contentInOther = Blob.getBlob(other.blobs.get(f)).contents;
                }
                String updatedContent = "<<<<<<< HEAD\n" + contentInHead
                        + "=======\n" + contentInOther + ">>>>>>>\n";
                writeContents(conflictFile, updatedContent);
                add(f);
            }
        }
        commit(String.format("Merged %s into %s.", branchName,
                branches.get("HEAD")), other.getSha1());
        if (conflictEncountered) {
            System.out.println("Encountered a merge conflict.");
        }
    }

    /** A helper method that finds the split point of the current branch
     * and the given branch. */
    public static Commit findSplitPoint(Commit head, Commit other) {
        // basic algorithm: use depth-first traversal to iterate HEAD's ancestors,
        // call them "potential(split point)", then check if any of them is the
        // ancestor of OTHER.
        Commit potentialOne = head;
        Commit potentialTwo = head;
        while (potentialOne != null || potentialTwo != null) {
            Commit parentOfOther = other;
            while (parentOfOther != null) {
                if (potentialOne != null && potentialOne.equals(parentOfOther)) {
                    return potentialOne;
                }
                if (potentialTwo != null && potentialTwo.equals(parentOfOther)) {
                    return potentialTwo;
                }
                parentOfOther = Commit.getCommit(parentOfOther.getParent());
            }
            if (potentialOne != null) {
                potentialOne = Commit.getCommit(potentialOne.getParent());
            }
            if (potentialTwo != null) {
                potentialTwo = Commit.getCommit(potentialTwo.getSecondParent());
            }
        }
//        Commit parentOfHead = head;
//        while (parentOfHead != null) {
//            Commit parentOfOther = other;
//            while (parentOfOther != null) {
//                if (parentOfHead.equals(parentOfOther)) {
//                    return parentOfHead;
//                }
//                parentOfOther = Commit.getCommit(parentOfOther.getParent());
//            }
//            parentOfHead = Commit.getCommit(parentOfHead.getParent());
//        }
        return null;
    }

}
