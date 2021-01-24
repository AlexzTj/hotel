# Hotel app

### How to test
```./gradlew test```
### How to run
```
./gradlew bootRun
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"premiumRoomsCount":"3","economyRoomsCount":"3"}' \
  http://localhost:8080/occupancy
```
example response:
```
{
  "premiumRoomData": {
    "expectedRevenue": 738,
    "usage": 3
  },
  "economyRoomData": {
    "expectedRevenue": 167,
    "usage": 3
  }
}
```
