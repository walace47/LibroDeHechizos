package com.joan.librohechizos.utiles;

/**
 * Created by Joan on 21/03/2017.
 */

public class ComunicadorDePersonajes {
    private static Object mensaje;


    public static Object getMensaje() {
        return mensaje;
    }

    public static void setMensaje(Object mensaje) {
        ComunicadorDePersonajes.mensaje = mensaje;
    }
}

