# Gitlet Design Document

**Name**: Oak

## Classes and Data Structures

### Commit

#### Instance Variables

* Message - contains the message of a commit.
* Timestamp - time at which a commit is created. Assigned by the constructor.
* Parent - the parent commit of a commit.
* Blobs - a hashmap tracking the blobs of the commit by mapping file names to the SHA-1 code of the blob objects.

### Repository

#### Methods

* init() - Creates a new Gitlet version-control system in the current directory.



## Algorithms

* commit(): 
    1.

## Persistence