package com.joan.librohechizos.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.provider.BaseColumns;
import android.util.Log;

import com.joan.librohechizos.modelo.Clase;
import com.joan.librohechizos.modelo.Hechizo;
import com.joan.librohechizos.modelo.Personaje;
import com.joan.librohechizos.modelo.Raza;
import com.joan.librohechizos.sqlite.Contratos.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by Giuliano on 06/03/2017.
 */

public class LibroHechizosBD extends SQLiteOpenHelper {

    private static final String NOMBRE_BASE_DATOS = "librohechizos.sqlite";

    private static final int VERSION_ACTUAL = 1;

    private final Context contexto;

    public LibroHechizosBD(Context context) {
        super(context, NOMBRE_BASE_DATOS, null, VERSION_ACTUAL);
        this.contexto = context;
    }

    interface Tablas {
        String PERSONAJE = "personaje";
        String CLASE = "clase";
        String RAZA = "raza";
        String HECHIZOS = "hechizos";
        String HECHIZOS_APRENDIDOS = "hechizos_aprendidos";
        String HECHIZOS_POR_CLASE = "hechizos_por_clase";
        String ESCUELA = "escuela";
    }

    interface Referencias {
        String ID_ESCUELA = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.ESCUELA, Escuelas.ID_ESCUELA);

        String ID_PERSONAJE = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.PERSONAJE, Personajes.ID_PERSONAJE);

        String ID_CLASE = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.CLASE, Clases.ID_CLASE);

        String ID_RAZA = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.RAZA, Razas.ID_RAZA);

        String ID_HECHIZOS = String.format("REFERENCES %s(%s) ON UPDATE CASCADE ON DELETE CASCADE",
                Tablas.HECHIZOS, Hechizos.ID_HECHIZO);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }

    private void precargarDatos(SQLiteDatabase db) {
        try {
            db.beginTransaction();
            //se cargan las clases
            precargarClases(db);
            //se cargan las razas
            db.execSQL("INSERT INTO raza(nombre) values('draconido')");
            db.execSQL("INSERT INTO raza(nombre) values('humano')");
            db.execSQL("INSERT INTO raza(nombre) values('gnomo')");
            db.execSQL("INSERT INTO raza(nombre) values('mediano')");
            db.execSQL("INSERT INTO raza(nombre) values('enano')");
            db.execSQL("INSERT INTO raza(nombre) values('elfo')");
            db.execSQL("INSERT INTO raza(nombre) values('genazi')");
            //se carga personaje de prueba
            db.execSQL("INSERT INTO personaje(nombre,id_clase,id_raza) values('leonidas',3,2)");
            //se cargan escuelas
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(1,'conjuracion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(2,'evocacion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(3,'abjuracion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(4,'ilusion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(5,'encantamiento')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(6,'necromancia')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(7,'transmutacion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            db.execSQL(String.format("INSERT INTO %s(%s, %s) values(8,'adivinacion')", Tablas.ESCUELA, Escuelas.ID_ESCUELA, Escuelas.NOMBRE));
            //se cargan hechizos
            precargarHechizos(db);

            precargarHechizoscsv(db);
            // db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (17,4)");
            //db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (17,5)");

            precargarHechizosXclaseCsv(db);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void precargarHechizos(SQLiteDatabase db) {
        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Umbral [Gate]', 'Conjuras un portal que une un espacio desocupado que puedas ver dentro del alcance a una ubicación precisa en un diferente plano de existencia. El portal es una abertura circular, que puedes hacer desde 5 hasta 20 pies (1 casilla hasta 4 casillas, 1.5 hasta 6 m) de diámetro. Puedes orientar el portal en cualquierdirección que elijas. El portal se mantiene por toda la duración del conjuro. El portal tiene una parte frontal y una parte trasera de cada plano donde esta aparece. Viajar a través del portal solo es posible moviéndose a través del frente. Cualquier cosa que lo haga es instantáneamente transportada al otro plano, apareciendo en el espacio desocupado más cercano del portal. Las Deidades y otros gobernantes planares pueden prevenir portales creados por este conjuro de abrirse en su presencia o en cualquier lugar de sus dominios. Cuando este conjuro es lanzado, puedes decir el nombre de una criatura especifica (un seudónimo, titulo, o un apodo no funcionan). Si la criatura está en otro plano que no sea en el que te encuentras, el portal se abre en los alrededores de la criatura nombrada y trae a la criatura hacia el espacio desocupado más cercano de tu lado del portal. No ganas ningún poder especial sobre la criatura y esta es libre de actuar como el DM lo juzgue apropiado. Podría irse, atacarte o ayudarte.', '', 60, 1, 1, 1, 'Un diamante con un valor mínimo de 5000po', 0, 1, '1 acción', 1, 9, 'hasta 1 minuto')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (1,10)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (1,4)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (1,5)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Ver lo invisible [See Invisibility]', 'Puedes ver criaturas y objetos como si fueran visibles, y puedes ver dentro del Plano Etéreo." +
                "Las criaturas etéreas y los objetos aparecen en forma espectral y son translúcidas.', '', 0, 1, 1, 1, 'Una pizca de talco o un poco de plata pulverizada', 0, 0, '1 acción', 8, 2, '1 hora')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (2,11)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (2,5)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (2,4)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Viajar mediante plantas [Transport Via" +
                "Plants]', 'El conjuro crea un vínculo mágico entre una planta inanimada grande o mayor, dentro del alcance, y otra planta, a cualquier distancia, en el mismo plano de existencia. Debes haber visto o tocado la planta destino al menos una" +
                "vez anteriormente. Durante la duración, cualquier criatura puede caminar dentro de la planta objetivo y salir por la planta de destino usando 5 pies (1 casilla, 1.5 m) de movimiento.', '', 10, 1, 1, 0, '', 0, 0, '1 acción', 1, 6, '1 turno')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (3,9)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Vida falsa [False Life]', 'Te fortaleces a ti mismo con una copia nigromántica de vida, ganas 1d4 + 4 Puntos de Golpe temporales mientras dure el conjuro.', 'Cuando lanzas este hechizo usando un espacio de conjuros de nivel 2 o superior, ganas 5 Puntos de Golpe temporales adicionales por cada nivel de espacio de conjuros por encima de nivel 1.', 0, 1, 1, 1, 'Una pequeña cantidad de alcohol o licores destilados', 0, 0, '1 acción', 6, 1,'1 hora')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (4,5)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (4,4)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Vínculo protector [Warding Bond]', 'Este conjuro protege a una criatura voluntaria que toques y crea una conexión mística entre el objetivo y tú hasta que el conjuro finalice. Mientras el objetivo se encuentre a 60 pies (12 casillas, 18 m) de ti, gana +1 de bonificación a la CA, en las tiradas de salvación, y tiene resistencia a todo el daño. También, cada vez que" +
                "sufra daño, tú sufres la misma cantidad. El conjuro finaliza si caes a 0 Puntos de Golpe o si el objetivo y tú estáis separados" +
                "más de 60 pies (12 casillas, 18 m). También finaliza si el conjuro es lanzado otra vez a cualquiera de las criaturas conectadas. Además puedes cancelar el conjuro como una acción.', '', 1, 1, 1, 1, 'Un par de anillos de platino de al menos 50 po de valor cada uno, los cuáles llevaréis tu objetivo y tú', 0, 0, '1 acción', 3, 2, '1 hora')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (5,10)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Virote encantado [Witch Bolt]', 'Un haz de chisporroteante energía azul es lanzado hacia una criatura dentro del alcance," +
                "formando un constante arco de relámpago entre el objetivo y tú. Haz un ataque de conjuro a distancia contra esa criatura. En un impacto, el objetivo sufre 1d12 puntos de daño por electricidad, y en cada uno de tus turnos mientras dure, puedes usar tu acción para realizar 1d12 puntos de daño por electrcidad al objetivo automáticamente. Este conjuro finaliza si usas tu acción para hacer otra cosa. Este conjuro también finaliza si el objetivo está alguna vez fuera del alcance del conjuro o si tiene cobertura total de ti.', 'Cuando lanzas este" +
                "hechizo usando un espacio de conjuros de nivel 2 o superior, el daño inicial se incrementa en 1d12 por cada nivel de espacio de conjuros por encima de nivel 1.', 30, 1, 1, 1, 'Una ramita de un árbol que ha sido alcanzada por un rayo', 0, 1, '1 acción', '2', 1, 'hasta 1 minuto')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (6,4)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (6,6)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Visión en la oscuridad [Darkvision]', 'Tocas a una criatura voluntaria para concederle la habilidad de ver en la oscuridad. Durante la duración del conjuro, la criatura elegida puede ver en la oscuridad hasta un alcance de 60 pies (12 casillas, 18 m).', '', 1, 1, 1, 1, 'Una pizca de zanahoria o un ágata', 0, 0, '1 acción', 7, 2, '8 horas')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (7,7)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (7,5)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (7,4)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (7,9)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Visión verdadera [True Seeing]', 'Este conjuro proporciona a la criatura voluntaria que tocas, la habilidad de ver las cosas" +
                "como realmente son. Mientras dure el conjuro, la criatura tiene visión verdadera, se da cuenta de puertas secretas ocultas por magia, y puede ver en el Plano Etéreo, todo en un alcance de 120 pies (24 casillas, 36 m).', '', 1, 1, 1, 1, 'Un ungüento para los ojos de 25po de valor; está hecho de polvo de setas, azafrán y grasa; que el conjuro consume', 0, 0, '1 acción', 8, 6, '1 hora')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (8,10)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (8,11)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (8,6)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (8,5)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (8,4)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Volar [Fly]', 'Tocas a una criatura voluntaria. El objetivo gana una velocidad de vuelo de 60 pies (12 casillas, 18 m) durante la duración del conjuro. Cuando el conjuro finaliza, si el objetivo aún está en el aire, cae a menos que pueda detener la caída.', 'Cuando lanzas este hechizo usando un espacio de conjuros de nivel 4 o superior, puedes elegir una criatura" +
                "adicional como objetivo por cada nivel de espacio de conjuros por encima de nivel 3', 1, 1, 1, 1, 'La pluma del ala de cualquier ave', 0, 1, '1 acción', 7, 3, 'hasta 10 minuto')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (9,4)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (9,5)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (9,6)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Zancada arbórea [Tree Stride]', 'Adquieres la habilidad de entrar en un árbol y moverte desde dentro del mismo hasta dentro de otro árbol del mismo tipo que se encuentre hasta a 500 pies (100 casillas, 150 m). Los dos árboles deben estar vivos y al menos tan grandes como tú. Debes usar 5 pies (1 casilla, 1.5m) de movimiento para entrar en el árbol. Conoces instantáneamente la localización de todos los árboles del mismo tipo a 500 pies (100 casillas, 150 m) de distancia y, como parte del movimiento usado para entrar en el árbol, puedes pasar dentro de uno de esos árboles o salir del árbol en el que estás. Apareces en un punto de tu elección a 5 pies (1 casilla, 1.5 m) del árbol destino, usando otros 5 pies (1 casilla, 1.5 m) de movimiento. Si no tienes suficiente movimiento, apareces a 5 pies (1 casilla, 1.5 m) del árbol en el que has entrado. Puedes usar esta habilidad de transporte una vez por asalto durante la duración. Debes terminar cada turno fuera de un árbol.', '', 0, 1, 1, 0, '', 0, 1, '1 acción', 1, 5, 'hasta 1 minuto')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (10,7)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (10,9)");

        db.execSQL("INSERT INTO hechizos(nombre, descripcion, a_mayor_nivel, rango, componente_verbal, componente_somatico, componente_material, descripcion_componente, ritual, concentracion, tiempo_de_casteo, escuela, nivel, duracion) VALUES ('Zona de verdad [Zone of Truth]', 'Creas una zona mágica que protege contra el engaño en una esfera de 15 pies (3 casillas, 4.5. m) de radio centrado en un lugar de tu elección dentro del alcance. Hasta que el conjuro finalice, una criatura que entre en el área del conjuro por primera vez en un turno o empiece su turno allí debe realizar una tirada de salvación de Carisma. Con una salvación fracasada, la criatura no puede decir una mentira deliberada mientras esté en el radio. Tú sabes si cada criatura hace la tirada de salvación con éxito o si la falla. Una criatura afectada es consciente del conjuro y por consiguiente puede evitar responder preguntas las cuales normalmente respondería con una mentira. Una criatura puede ser evasiva en sus respuestas siempre y cuando permanezca dentro del límite de la verdad.', '', 60, 1, 1, 0, '', 0, 0, '1 acción', 1, 2, '10 minutos')");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (11,11)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (11,10)");
        db.execSQL("INSERT INTO " + Tablas.HECHIZOS_POR_CLASE + "(" + HechizosPorClases.ID_HECHIZO + "," + HechizosPorClases.ID_CLASE + ") VALUES (11,2)");


    }


    public void precargarClases(SQLiteDatabase db) {
        InputStream inStream = getClass().getClassLoader().getResourceAsStream("assets/Clases.csv");
        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        try {
            buffer.readLine();
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split(",");
                if (colums.length == 2) {
                    ContentValues cv = new ContentValues(2);
                    cv.put(Clases.ID_CLASE, colums[0].trim());
                    cv.put(Clases.NOMBRE, colums[1].trim());
                    db.insert(Tablas.CLASE, null, cv);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void precargarHechizoscsv(SQLiteDatabase db) {
        InputStream inStream = getClass().getClassLoader().getResourceAsStream("assets/Hechizosl.csv");
        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(inStream, "cp1252"));
        } catch (UnsupportedEncodingException e) {
            System.out.printf("no anda");
            e.printStackTrace();
        }
        String line = "";
        try {
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split("&");
                if (colums.length == 15) {
                    ContentValues cv = new ContentValues();
                    cv.put(Hechizos.ID_HECHIZO, colums[0].trim());
                    cv.put(Hechizos.NOMBRE, colums[1].trim());
                    String descripcion = colums[2].trim();
                    descripcion = descripcion.replaceAll("<br>", "</p><p>");
                    cv.put(Hechizos.DESCRIPCION, descripcion);
                    cv.put(Hechizos.A_MAYOR_NIVEL, colums[3].trim());
                    cv.put(Hechizos.RANGO, colums[4].trim());
                    cv.put(Hechizos.COMPONENTE_VERBAL, colums[5].trim());
                    cv.put(Hechizos.COMPONENTE_SOMATICO, colums[6].trim());
                    cv.put(Hechizos.COMPONENTE_MATERIAL, colums[7].trim());
                    cv.put(Hechizos.DESCRIPCION_COMPONENTE, colums[8].trim());
                    cv.put(Hechizos.RITUAL, colums[9].trim());
                    cv.put(Hechizos.CONCENTRACION, colums[10].trim());
                    cv.put(Hechizos.TIEMPO_DE_CASTEO, colums[11].trim());
                    cv.put(Hechizos.ESCUELA, colums[12].trim());
                    cv.put(Hechizos.NIVEL, colums[13].trim());
                    cv.put(Hechizos.DURACION, colums[14].trim());
                    db.insert(Tablas.HECHIZOS, null, cv);

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void precargarHechizosXclaseCsv(SQLiteDatabase db) {
        InputStream inStream = getClass().getClassLoader().getResourceAsStream("assets/HechizoxClase.csv");
        ;

        BufferedReader buffer = null;
        try {
            buffer = new BufferedReader(new InputStreamReader(inStream, "cp1252"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            //buffer.readLine();
            while ((line = buffer.readLine()) != null) {
                String[] colums = line.split("&");
                // if (colums.length != 12) {
                //   Log.d("CSVParser", "Skipping Bad CSV Row");
                // continue;
                // }
                ContentValues cv = new ContentValues();
                cv.put(HechizosPorClases.ID_HECHIZO, colums[0].trim());
                cv.put(HechizosPorClases.ID_CLASE, colums[1].trim());


                db.insert(Tablas.HECHIZOS_POR_CLASE, null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL,%s INTEGER NOT NULL %s, %s INTEGER NOT NULL %s)",
                Tablas.PERSONAJE,
                Personajes.ID_PERSONAJE,
                Personajes.NOMBRE,
                Personajes.ID_CLASE, Referencias.ID_CLASE,
                Personajes.ID_RAZA, Referencias.ID_RAZA));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL)",
                Tablas.CLASE,
                Clases.ID_CLASE,
                Clases.NOMBRE));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL)",
                Tablas.RAZA,
                Razas.ID_RAZA,
                Razas.NOMBRE));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL)",
                Tablas.ESCUELA,
                Escuelas.ID_ESCUELA,
                Escuelas.NOMBRE));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "%s TEXT NOT NULL,%s TEXT NOT NULL, %s TEXT NOT NULL ,%s INTEGER NOT NULL," +
                        "%s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL," +
                        " %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s TEXT NOT NULL, %s INTEGER NOT NULL %s, %s INTEGER NOT NULL, %s TEXT NOT NULL)",
                Tablas.HECHIZOS,
                Hechizos.ID_HECHIZO,
                Hechizos.NOMBRE,
                Hechizos.DESCRIPCION,
                Hechizos.A_MAYOR_NIVEL,
                Hechizos.RANGO,
                Hechizos.COMPONENTE_VERBAL,
                Hechizos.COMPONENTE_SOMATICO,
                Hechizos.COMPONENTE_MATERIAL,
                Hechizos.DESCRIPCION_COMPONENTE,
                Hechizos.RITUAL,
                Hechizos.CONCENTRACION,
                Hechizos.TIEMPO_DE_CASTEO,
                Hechizos.ESCUELA, Referencias.ID_ESCUELA,
                Hechizos.NIVEL,
                Hechizos.DURACION));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER NOT NULL %s," +
                        "%s INTEGER NOT NULL %s, PRIMARY KEY(%s,%s))",
                Tablas.HECHIZOS_POR_CLASE,
                HechizosPorClases.ID_CLASE, Referencias.ID_CLASE,
                HechizosPorClases.ID_HECHIZO, Referencias.ID_HECHIZOS,
                Clases.ID_CLASE, Hechizos.ID_HECHIZO));

        db.execSQL(String.format("CREATE TABLE %s (%s INTEGER NOT NULL %s, " +
                        "%s INTEGER NOT NULL %s, %s INTEGER NOT NULL , PRIMARY KEY(%s,%s))",
                Tablas.HECHIZOS_APRENDIDOS,
                HechizosAprendidos.ID_PERSONAJE, Referencias.ID_PERSONAJE,
                HechizosAprendidos.ID_HECHIZO, Referencias.ID_HECHIZOS,
                HechizosAprendidos.PREPARADO,
                Personajes.ID_PERSONAJE, Hechizos.ID_HECHIZO));
        precargarDatos(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion < oldVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.PERSONAJE);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.CLASE);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.RAZA);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.HECHIZOS);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.HECHIZOS_APRENDIDOS);
            db.execSQL("DROP TABLE IF EXISTS " + Tablas.HECHIZOS_POR_CLASE);
        }
    }


}
