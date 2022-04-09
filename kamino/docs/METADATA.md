# Annotation

> @Metadata(name = "TEST_NAME" (optional), containerType = Class<? extends MetadataContainer>)

## Name
Optional name which overrides the field name, which is basically the name of the json element in the metadata database container

### Example

@Metadata(containerType = StringMetadataContainer.class)
public final String playerID;

-> Name of property in database = playerID

@Metadata(name = "player", containerType = StringMetadataContainer.class)
public final String playerID;

-> Name of property in database = player

## Container

Metadata Container are different ways to save the variable, the most basic one is a set variant 

### Example

StringMetadataContainer: 
Uses a Hashset to uniquely identify every used variable only once

List of uuids in event data: UUID1, UUID2, UUID1, UUID4, UUID65, UUID1 \
Metadata (StringMetadataContainer): UUID1, UUID2, UUID4, UUID65

### Custom Container 

Required: No Args Constructor, extend MetadataContainer

# Bucket

# Metadata Container

{
    "serverIP": "168.0.1.67",
    "amountOfEvents":4500,
    "timestamp-min":1649359692802,
    "timestamp-max":1649359692802, // MAXIMUM OF 5 MINUTES OR 5000 EVENTS ?
    metadata: {
        eventClasses: [ BlockBreakEvent, PlayerJoinEvent, PlayerQuitEvent ]
        playerIDs: [ "UUID1", "UUID2", "UUID3" ]
        blockIDs: [ 1, 2, 67, 34, 90 ]
        area: { min: { 0, 10 }, max: { 10, 90 } }
        worldNames: [ "main" ]
    },
    dataID: "UUID of Data Collection, which contains all the events in one huge JSON"
}