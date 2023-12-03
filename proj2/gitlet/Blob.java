package gitlet;

import java.io.File;
import java.io.Serializable;

import static gitlet.Utils.*;

/** Represent a blob object, i.e. the saved contents of files. */
public class Blob implements Serializable {
    /** The file that the blob represents. */
    File f;
    /** The actual contents in the file that the blob represents. */
    String contents;

    public Blob(File f) {
        this.f = f;
        this.contents = readContentsAsString(f);
    }

    /** Save the blob as a file in the disk with the file name being its SHA-1 code. */
    public void saveBlob() {
        File blobText = join(Repository.GITLET_DIR, sha1(serialize(this)) + ".txt");
        writeObject(blobText, this);
    }

    /** Get the blob object from the disk using its SHA-1 code. */
    public static Blob getBlob(String sha1) {
        File f = join(Repository.GITLET_DIR, sha1 + ".txt");
        return readObject(f, Blob.class);
    }

}
