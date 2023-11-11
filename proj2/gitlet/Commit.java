package gitlet;

// TODO: any imports you need here

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date; // TODO: You'll likely use this in this class
import java.util.Calendar;
import java.util.HashMap;

import static gitlet.Utils.*;
import static gitlet.Utils.writeObject;

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
    /** A map: File name -> Blob SHA-1 */
    public HashMap<String, String> blobs;

    public Commit(String message, String parent) {
        this.message = message;
        this.parent = parent;
        if (this.parent.isEmpty()) {
            this.timestamp = new Date(0);
        } else {
            this.timestamp = new Date();
        }
        this.blobs = new HashMap<>();
    }

    public String getParent() {
        return this.parent;
    }
    public Date getTimestamp() {
        return timestamp;
    }
    public String getMessage() {
        return message;
    }
    /** Save the commit as a file in the disk with the file name being its SHA-1 code. */
    public void saveCommit() throws IOException {
        File f = join(Repository.GITLET_DIR, sha1(serialize(this)) + ".txt");
        f.createNewFile();
        writeObject(f, this);
    }
    /** Get the commit object from the disk using its SHA-1 code. */
    public static Commit getCommit(String sha1) {
        File f = join(Repository.GITLET_DIR, sha1 + ".txt");
        return readObject(f, Commit.class);
    }

}
