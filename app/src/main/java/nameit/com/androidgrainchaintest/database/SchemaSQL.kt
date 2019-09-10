package nameit.com.androidgrainchaintest.database

class SchemaSQL {
    companion object {
        var MtoCat_Routes = "CREATE TABLE " + InfoDB.NAME_TABLE +
                "(RouteID INTEGER PRIMARY KEY AUTOINCREMENT, NameRoute TEXT, InitLat TEXT, InitLon TEXT, FinalLat TEXT, FinalLon TEXT, Distance TEXT, Time TEXT)"
    }
}