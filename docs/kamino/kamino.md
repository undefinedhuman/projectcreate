# Kamino

# Installation

Put the jar file in your servers plugin directory!

## Config
Database HOST
Database USER
Database PASSWORD
Database Bucket Name

TLS: create kamino.cert and place your couchbase certificate in the file, and set tls in config to true

## Database (Couchbase) Setup

Create a couchbase cluster 

### Recommendations:

Even thought the plugin the can create the required buckets, scopes and collections by itself, if the supplied user has the required permissions. 
It is recommended to setup a bucket by default named "projectcreate" (configurable via kamino.config) and set the appropriate amount of available memory.