package com.joan.librohechizos.utiles;

/**
 * Created by Joan on 12/03/2017.
 */

public class ComunicadorDeHechizo {
    private static Object mensaje;


    public static Object getMensaje() {
        return mensaje;
    }

    public static void setMensaje(Object mensaje) {
        ComunicadorDeHechizo.mensaje = mensaje;
    }
}
