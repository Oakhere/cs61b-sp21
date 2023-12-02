package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Formatter;
import java.util.HashMap;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *  TODO: It's a good idea to give a description here of what else this Class
 *  does at a high level.
 *
 *  @author TODO
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The time this Commit was created. */
    private Date timestamp;
    /** The SHA-1 code of the parent of this Commit. */
    private String parent;
    private String secondParent;
    private String sha1;
    /** A map: File name -> Blob SHA-1 */
    public HashMap<String, String> blobs;

    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        this.secondParent = "";
        if (this.parent.isEmpty()) {
            this.timestamp = new Date(0);
        } else {
            this.timestamp = new Date();
        }
        this.blobs = new HashMap<>();
        this.sha1 = "";
    }

    /** A special constructor for merge commits. */
    public Commit(String message, String parent, String secondParent) {
        this.message = message;
        this.parent = parent;
        this.secondParent = secondParent;
        this.timestamp = new Date();
        this.blobs = new HashMap<>();
        this.sha1 = "";
    }

    public String getParent() {
        return this.parent;
    }
    public String getSecondParent() {
        return this.secondParent;
    }
    public String getMessage() {
        return message;
    }
    public String getSha1() {
        return sha1;
    }

    /** Save the commit as a file in the disk with the file name being its SHA-1 code. */
    public void saveCommit(String sha1) {
        this.sha1 = sha1;
        File f = join(Repository.GITLET_DIR, sha1 + ".txt");
        writeObject(f, this);
    }
    /** Get the commit object from the disk using its SHA-1 code. */
    public static Commit getCommit(String sha1) {
        // special case for trying to get the parent of the initial commit
        if (sha1.isEmpty()) {
            return null;
        }
        File f = join(Repository.GITLET_DIR, sha1 + ".txt");
        return readObject(f, Commit.class);
    }

    /** A helper method for the command log and global-log. */
    public void printLog() {
        Formatter formatter = new Formatter();
        System.out.println("===");
        System.out.println("commit " + sha1);
        // merge commits have an extra line of information
        if (!secondParent.isEmpty()) {
            System.out.println(String.format("Merge: %s %s", parent.substring(0, 7),
                    secondParent.substring(0, 7)));
        }
        formatter.format("Date: %ta %<tb %<td %<tT %<tY %tz", timestamp, timestamp);
        System.out.println(formatter);
        System.out.println(message);
        System.out.println();
        formatter.close();
    }

    /** Return true if the two commits are the same commit. */
    @Override
    public boolean equals(Object other) {
        if (other.getClass() != Commit.class) {
            return false;
        }
        return this.sha1.equals(((Commit) other).getSha1());
    }
}
