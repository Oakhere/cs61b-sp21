package gitlet;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import static gitlet.Utils.*;


public class StagingArea implements Serializable {
    /** Mapping file name -> SHA-1 code of the blob. */
    HashMap<String, String> blobsForAddition;
    /** Contains the filenames of the files which should be removed. */
    HashSet<String> blobsForRemoval;

    public StagingArea() {
        this.blobsForAddition = new HashMap<>();
        this.blobsForRemoval = new HashSet<>();
    }

    /** Save the file as a blob, and add the blob to the staging area. */
    public void add(File f) {
        Blob blob = new Blob(f);
        blob.saveBlob();
        blobsForAddition.put(f.getName(), sha1(serialize(blob)));
    }

    /** Clear the staging area. */
    public void clear() {
        this.blobsForAddition.clear();
        this.blobsForRemoval.clear();
    }

    /** Return true if there's no staged additions/removals. */
    public boolean isEmpty() {
        return blobsForAddition.isEmpty() && blobsForRemoval.isEmpty();
    }

}
