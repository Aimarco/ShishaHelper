package Models

import java.io.Serializable

class SaboresTabaco (
    var idSabor : String,
    var idTabaco : String,
    var nombre:String,
    var descripcion:String): Serializable
{

    constructor():this("","","","")
}