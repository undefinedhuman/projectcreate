# Kamino

> SE_06 

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
Kamino saves a fixed maximum amount of events in one bucket, which is either saved after a fixed amount of time (default: 5 minutes) or when the bucket reached full capacity.
For each event in the bucket metadata is extracted; which is then saved in an extra metadata bucket; afterwards the event bucket gets
compressed and saved, currently the compression ratio is on average 2.
Compression ratio can however be greatly impacted by different factors, and configurations need to be done for specific use cases.
Some factors that I found highly impact the compression rate, and that will be tweak able in the future:

Maximum amount of events per buckets - higher chance of repetitive events \
Amount of player/Amount of Events per 5 Minutes - Increase/Decrease time after which bucket will be saved based on expected server usage \
Compression Algorithm - Currently the only option is lz4, later: Multiple selectable algorithms

### Example 
Generated event data (currently not accurate real data, will be possible when the server supports a wider range of event types) \
Max events per bucket: 10000 \
Amount of players: 1000

#### Compressed

| Player | Hours | Amount of Buckets generated | Disk utilization |
|--------|-------|-----------------------------|------------------|
| 1000   | 1     | 722                         | 182.998MiB       |
| 1000   | 2     | 1354                        | 366.05MiB        |
| 1000   | 3     | 2290                        | 548.706MiB       |

#### Uncompressed

| Player | Hours | Amount of Buckets generated | Disk utilization |
|--------|-------|-----------------------------|------------------|
| 1000   | 1     | 722                         | 328.33MiB        |
| 1000   | 2     | 1354                        | 656.084MiB       |
| 1000   | 3     | 2290                        | 984.6MiB         |

## Metadata