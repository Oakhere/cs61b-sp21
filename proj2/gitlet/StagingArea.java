package gitlet;

import java.io.File;
import java.io.IOException;
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
    public void add(File f) throws IOException {
        Blob blob = new Blob(f);
        blob.saveBlob();
        blobsForAddition.put(f.getName(), sha1(serialize(blob)));
    }

    public void remove(File file) {

    }

}
