package com.joan.librohechizos.sqlite;

import java.util.UUID;

/**
 * Created by Giuliano on 06/03/2017.
 */

public class Contratos {

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

}
