# Kamino

> SE_06 First paragraphs explain the project and the installation; compression is partly relevant for the module, the description of the collections and indices starts with metadata.

Imagine running a commercial server; your players are doing all sorts of things and encountering or pretending to encounter all kinds of issues. Suppose the server has some precious items, or actually any item; a player (undefinedplayer1) puts one of them in a chest
and then is offline for a few days. His friend (undefinedplayer2) takes the item, then after a couple of days, undefinedplayer1 reconnects but does not communicate with undefinedplayer2 and looks for his item. After freaking out, he contacts an admin:

My item is gone, I put it in that chest over there, and now it's gone. Give it back NOW.

Perhaps the simplest solution for the admin would be to say the server has a zero-tolerance policy for replacing missing in-game objects of any kind.
However, such a policy would probably be the worst solution. It would likely cause undefinedplayer1 to leave immediately and cause undefinedplayer2 to leave the server because his friend is no longer online. And there could be similar cases where there might even be a bug that caused the item to disappear, or a player is actively trying to trick the system by retrieving items that never existed in the first place.

Kamino attempts to solve this problem by collecting all in-game events, such as inventory movements, and storing them for a fixed time. A type of logger for in-game events. It provides a mechanism for admins to search for specific events within certain time frames and specify certain parameters; for instance, the admin could search for inventory move events with the PlayerID of undefinedplayer1 to check if the item was placed precisely where it was told to be. And then, he could filter for inventory move events with the specific ItemID to find the event that caused undefinedplayer2 to remove the item from the chest. Or search all events in the last 90 days (default time frame for logging); though this would be time-consuming, I suppose it's also possible.

## Table of Contents
1. [Installation](#Installation)
2. [Configuration](#configuration)
3. [Couchbase](#couchbase-setup)
4. [Compression](#compression)
5. [Metadata](#metadata)
6. [Indexation](#indexation)

## Installation

Place the jar file (kamino.jar) in your servers' plugin directory.
Can usually be found in the same directory of the server.jar,
and for development if you followed the local development setup is: 

> ~/.projectcreate/server/plugins/kamino.jar.

Start the server as usual, when setting it up for the first time, stop it again as soon as possible and configure the database connection.

## Configuration

* DATABASE_URL=127.0.0.1
* DATABASE_USER=undefinedhuman      
 -> Must be able to insert and read documents in the specified collection; if not configured manually, must be able to create the bucket, scope and collection.

* DATABASE_PASSWORD=V3RY_S3CUR3_P4SSW0RD

* DATABASE_BUCKET_NAME=projectcreate   
 -> If not manually created, the plugin does it.

* NUMBER_OF_THREADS=1  
  -> Specifies the amount of threads used for processing the buckets of event which will be saved to the database.  
  -> >= 1

## Couchbase Setup

Couchbase cluster (min version 7)

https://docs.couchbase.com/server/current/install/install-intro.html

### Recommendation:

Even though the plugin can create the required buckets, scopes and collections itself, if the specified user has the required permissions.
It is recommended to set up a bucket with the name "projectcreate" per standard (configurable via kamino.config) and to specify the appropriate amount of available memory.

## Compression
Kamino stores a fixed maximum number of events in a bucket, which is saved either after a certain time (default: 5 minutes) or when the bucket is full.
For each event in the bucket, metadata is extracted -> the metadata bucket is stored separately -> the event bucket is compressed and stored, with an average compression rate of currently 2.

However, the compression ratio can be greatly influenced by various factors, and configurations for specific use cases will be possible in the future.
I have found that some factors have a great influence on the compression ratio:

Maximum amount of events per buckets - higher chance of repetitive events \
Amount of player/Amount of Events per 5 Minutes - Increase/Decrease time after which bucket will be saved based on expected server usage \
Compression Algorithm - Currently the only option is lz4, later: Multiple selectable algorithms

### Example 
Generated event data (estimated events, not accurate real data, will be possible when the server supports a wider range of event types) \
Max events per bucket: 10000 \
Amount of players: 1000

#### Compressed

| Player | Hours | Buckets generated | Events generated | Disk utilization |
|--------|-------|-------------------|------------------|------------------|
| 1000   | 1     | 722               | 7220645          | 182.998MiB       |
| 1000   | 2     | 1354              | 13541893         | 366.05MiB        |
| 1000   | 3     | 2290              | 22905431         | 548.706MiB       |

#### Uncompressed

| Player | Hours | Buckets generated | Events generated | Disk utilization |
|--------|-------|-------------------|------------------|------------------|
| 1000   | 1     | 722               | 7220645          | 328.33MiB        |
| 1000   | 2     | 1354              | 13541893         | 656.084MiB       |
| 1000   | 3     | 2290              | 22905431         | 984.6MiB         |

## Event structure
Kamino logs all events that extend the engine's [event](https://gitlab.playprojectcreate.com/undefinedhuman/project-create/-/blob/dev/engine/src/de/undefinedhuman/projectcreate/engine/event/Event.java) class and are registered with Kamino during server runtime. While Kamino itself records all-important game/server events, external plugins can also create and report their own events. As a result, Kamino needs a dynamic system to register, parse, store and search events that are unknown at compile time of the plugin.

#### Example (BlockBreakEvent):

```java
public class BlockBreakEvent extends WorldItemEvent {

    @Metadata(databaseName = "area", containerType = AreaMetadataContainer.class)
    public final Vector2 position;
    @Metadata
    public final String playerUUID;

    public BlockBreakEvent(int itemID, String worldName, Vector2 position, String playerUUID) {
        super(itemID, worldName);
        this.position = position;
        this.playerUUID = playerUUID;
    }

}
```

## Metadata
Each non-static field in the event class and its parent classes is stored in the JSON object of the event. If a field has a metadata annotation, it is parsed and stored in the metadata area associated with the event bucket. How it is parsed is defined by the metadata annotation. Currently, there are two options (containerType). However, this will be extended in the future, and again any external plugin can implement its own systems.

Fields should be provided as metadata if you want to query them efficiently. (It will also be possible to query fields without metadata in later versions of the plugin). However, the data should be highly repetitive to avoid bloating the metadata bucket. Player IDs are perfect as metadata. There are a fixed number of them. Each player usually produces a number much larger than 1 event per metadata bucket, making them highly repetitive.

> SE_06 Specific information: Each Metadata bucket contains a dynamic amount of metadata container data.  

### Annotation
```java
@Metadata(databaseName = (String), containerType = (Class extends MetadataContainer), index = (String))
```
* databaseName = Field name used in the database, optional, default: FIELD_NAME + "s"
* containerType = Class which extends MetadataContainer, defines the way the field value is stored in the metadata bucket, optional, default: BasicMetadataContainer
* index = Define a secondary database index, optimize performance for this specific field, optional 

#### BasicMetadataContainer:
The container saves the value of each field in a HashSet, which removes all duplicates.

#### AreaMetadataContainer: 
Positions tend not to repeat that often, so the area container analyses each position in the events and creates an area that merges all of them.

Since this is relatively inefficient and the world is later divided into chunks, all chunks in which an event has occurred will be stored instead.

#### Example

Metadata Document

```json
{
    "serverIP": "168.0.1.67",
    "amountOfEvents":4500,
    "timestamp-min":1649359692802,
    "timestamp-max":1649359692802,
    "metadata": {
        "eventClasses": [ 
          "de.undefinedhuman.projectcreate.kamino.event.events.entity.EntitySpawnEvent", 
          "de.undefinedhuman.projectcreate.kamino.event.events.player.PlayerJoinEvent", 
          "de.undefinedhuman.projectcreate.kamino.event.events.block.BlockBreakEvent",
          ...
        ],
        "playerIDs": [ 
          "3f2041d5-62b5-4859-892c-535ab5bfa04a", 
          "81d37e88-fa1e-495a-99b1-cd3ca8d29f2f", 
          "9773cc7c-ea19-46ce-b2b6-96adba6e79a2",
          ...
        ],
        "blockIDs": [ 
          1, 2, 67, 34, 90, ... 
        ],
        "area": { 
          "min": { 
            "x": 0, 
            "y": 10
          }, 
          "max": { 
            "x": 10, 
            "y": 90
          }
        },
        "worldNames": [ 
          "Main", 
          ... 
        ]
    },
    "eventBucketID": "65a41e88-fa1e-432a-99b1-cd4fa8d29f2f"
}
```

Event Document

```json
[
  {
    "playerUUID": "81d37e88-fa1e-495a-99b1-cd3ca8d29f2f",
    "blueprintID": 0,
    "eventType": "de.undefinedhuman.projectcreate.kamino.event.events.player.PlayerJoinEvent",
    "timestamp": 1651087830042
  },
  {
    "worldName": "Main",
    "blueprintID": 9,
    "eventType": "de.undefinedhuman.projectcreate.kamino.event.events.entity.EntitySpawnEvent",
    "timestamp": 61609183200000
  },
  {
    "position": {
      "x": 1000.0,
      "y": 1125.0
    },
    "playerUUID": "3f2041d5-62b5-4859-892c-535ab5bfa04a",
    "worldName": "Main",
    "itemID": 46,
    "eventType": "de.undefinedhuman.projectcreate.kamino.event.events.block.BlockBreakEvent",
    "timestamp": 61609183200000
  },
  {
    "position": {
      "x": 2036.0,
      "y": 1170.0
    },
    "playerUUID": "9773cc7c-ea19-46ce-b2b6-96adba6e79a2",
    "worldName": "Main",
    "itemID": 21,
    "eventType": "de.undefinedhuman.projectcreate.kamino.event.events.block.BlockBreakEvent",
    "timestamp": 61609183200000
  },
  ...
]
```

## Indexation
Both collections (bucket types) contain a primary index that allows queries with all available fields. A secondary index can be created by any metadata field, which will be used automatically when queries are made against that field.

> Again: Each external plugin can specify their own indicies for their metadata fields.

### Example
As an example showcase, the secondary index used for the eventType field in the metadata bucket:

Indexing of buckets/documents is done separately according to the event types used in the bucket, which enables much faster retrieval of less frequently used event types.

```N1QL
CREATE INDEX eventTypes ON projectcreate.kamino.meta((distinct (array id for id in (metadata.eventTypes) end)))
```

> Parameters: 10 Hours, 1000 Players, 7154 Buckets generated, 1.8GB Disk utilization, 100 MB Secondary Index size

| EventType       | Primary Index only | Primary + Secondary Index |
|-----------------|--------------------|---------------------------|
| BlockBreakEvent | average 1s         | average 1s                |
| PlayerJoinEvent | average 1s         | average 3.5ms             |