package Models

import java.io.Serializable


class MarcaTabaco (
    var id:String,
    var nombre:String,
    var imagen:String) : Serializable
{

    constructor():this("","","")
}
