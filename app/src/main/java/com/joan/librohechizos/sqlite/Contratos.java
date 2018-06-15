package com.joan.librohechizos.sqlite;

import java.util.UUID;

/**
 * Created by Giuliano on 06/03/2017.
 */

public class Contratos {

    interface ColumnasDote{
        String ID_DOTE = "id_dote";
        String NOMBRE = "nombre";
        String DESCRIPCION = "descripcion";
        String REQUISITOS = "requisitos";
    }


    interface ColumnasPersonaje {
        String ID_PERSONAJE = "id_personaje";
        String NOMBRE = "nombre";
        String ID_CLASE = "id_clase";
        String ID_RAZA = "id_raza";
    }

    interface ColumnasClase {
        String ID_CLASE = "id_clase";
        String NOMBRE = "nombre";
    }

    interface ColumnasRaza {
        String ID_RAZA = "id_raza";
        String NOMBRE = "nombre";
    }

    interface ColumnasHechizos{
        String ID_HECHIZO="id_hechizo";
        String NOMBRE="nombre";
        String RANGO="rango";
        String ESCUELA="escuela";
        String NIVEL="nivel";
        String TIEMPO_DE_CASTEO="tiempo_de_casteo";
        String DURACION="duracion";
        String CONCENTRACION="concentracion";
        String  RITUAL="ritual";
        String COMPONENTE_VERBAL="componente_verbal";
        String COMPONENTE_SOMATICO="componente_somatico";
        String COMPONENTE_MATERIAL="componente_material";
        String DESCRIPCION_COMPONENTE="descripcion_componente";
        String DESCRIPCION="descripcion";
        String A_MAYOR_NIVEL="a_mayor_nivel";
    }

    interface ColumnasHechizosAprendidos{
        String ID_PERSONAJE="id_personaje";
        String ID_HECHIZO="id_hechizo";
        String PREPARADO="preparado";
    }

    interface ColumnasHechizosPorClases{
        String ID_CLASE="id_clase";
        String ID_HECHIZO="id_hechizo";

    }
    interface ColumnaEscuela{
        String ID_ESCUELA="id_escuela";
        String NOMBRE="nombre";
    }


    public static class HechizosPorClases implements ColumnasHechizosPorClases{

    }
    public static class Escuelas implements ColumnaEscuela{

    }
    public static class HechizosAprendidos implements ColumnasHechizosAprendidos{

    }

    public static class Dotes implements ColumnasDote{

    }

    public static class Personajes implements ColumnasPersonaje {
        public static String generarIdPersonaje() {
            return "P-"+ UUID.randomUUID().toString();
        }
    }

    public static class Clases implements ColumnasClase {
        public static String generarIdClase() {
            return "C-"+ UUID.randomUUID().toString();
        }
    }

    public static class Razas implements ColumnasRaza {
        public static String generarIdRaza() {
            return "R-"+ UUID.randomUUID().toString();
        }
    }

    public static class Hechizos implements ColumnasHechizos{
        public static String genetarIdHechizo(){
            return "H-"+ UUID.randomUUID().toString();
        }
    }

}
