App preview Youtube link: https://youtu.be/kHaJwPglBfI

App to search recipes using recipepuppy.com API.

#ViewModel #Retrofit2 #MVVM #Activity #LiveData

INFO 

Guía de arquitectura de apps
Esta guía está destinada a los desarrolladores que ya conocen los aspectos básicos de cómo crear una app y ahora quieren conocer las recomendaciones y la arquitectura sugerida para desarrollar apps sólidas y de calidad de producción.

En esta página, se asume que estás familiarizado con el framework de Android. Si recién te inicias en el desarrollo de apps de Android, consulta nuestras Guías para desarrolladores, que incluyen los temas que son requisitos previos para esta guía.

Experiencias del usuario de apps para dispositivos móviles
En la mayoría de los casos, las apps de escritorio tienen un solo punto de entrada desde un escritorio o un selector de programas y, luego, se ejecutan como un solo proceso monolítico. Las apps de Android, por otro lado, tienen una estructura mucho más compleja. Una app de Android típica consta de varios componentes de la app, como actividades, fragmentos, servicios, proveedores de contenido y receptores de emisión.

La mayoría de estos componentes de la app están declarados en el manifiesto de la app. Luego, el SO Android usa este archivo para decidir cómo integrar tu app a la experiencia del usuario general del dispositivo Debido a que una app de Android escrita correctamente costa de varios componentes y dado que los usuarios suelen interactuar con varias apps en un período breve, las apps deben adaptarse a distintos tipos de tareas y flujos de trabajo controlados por los usuarios.

Por ejemplo, piensa en lo que sucede cuando compartes una foto en la app de tu red social favorita.

La app activa un intent de cámara. Luego, el SO Android inicia una app de cámara para responder a la solicitud.

En este momento, el usuario deja la app de la red social, pero su experiencia está perfectamente integrada.

La app de cámara, a su vez, puede activar otros intents, como iniciar el selector de archivos, que puede iniciar otra app.

Eventualmente, el usuario vuelve a la app de la red social y comparte la foto.

En cualquier momento del proceso, el usuario podría recibir una llamada o una notificación. Después de realizar la acción que corresponda, el usuario espera poder volver al proceso de compartir fotos y reanudarlo. Este comportamiento de pasar de una app a la otra es muy común en los dispositivos móviles, de manera que tu app debe poder manejar estos flujos correctamente.

Ten en cuenta que los dispositivos móviles tienen restricciones de recursos, de manera que, en cualquier momento, el sistema operativo podría cerrar algunos procesos de app a fin de hacer lugar para otros.

Dadas las condiciones de este entorno, es posible que los componentes de tu app se inicien de manera individual y desordenada, además de que el usuario o el sistema operativo podrían destruirlos en cualquier momento Debido a que no puedes controlar estos eventos, no deberías almacenar datos ni estados de la app en los componentes de tu app, y los componentes de tu app no deben ser interdependientes.

Principios comunes de la arquitectura
Si no deberías usar los componentes de la app para almacenar datos y estados, ¿cómo deberías diseñar tu app?

Separación de problemas
El principio más importante que debes seguir es el de separación de problemas. Un error frecuente consiste en escribir todo tu código en una Activity o en un Fragment. Estas clases basadas en IU solo deberían contener lógica que se ocupe de interacciones del sistema operativo y de IU. Si mantienes estas clases tan limpias como sea posible, puedes evitar muchos problemas relacionados con el ciclo de vida.

Ten en cuenta que no eres el propietario de las implementaciones de Activity y Fragment, sino que solo son clases que representan el contrato entre el SO Android y tu app. El SO puede destruirlas en cualquier momento en función de las interacciones de usuarios y otras condiciones del sistema como memoria insuficiente. Para brindar una experiencia del usuario satisfactoria y una experiencia de mantenimiento de apps más fácil de administrar, recomendamos reducir la dependencia de esas apps.

Cómo controlar la IU a partir de un modelo
Otro principio importante indica que debes controlar la IU a partir de un modelo, preferentemente un modelo de persistencia. Los modelos son componentes responsables de manejar los datos para la app. Son independientes de los componentes de la app y los objetos de View, de modo que no se ven afectados por el ciclo de vida de la app y los problemas asociados.

La persistencia es ideal debido a los siguientes motivos:

Tus usuarios no perderán datos si el SO Android destruye tu app para liberar recursos.
Tu app continúa funcionando cuando una conexión de red es débil o no está disponible.
Si tu app está basada en clases de modelos con una responsabilidad bien definida de administrar los datos, tu app será más consistente y será más fácil realizar pruebas en ella.

Arquitectura de app recomendada
En esta sección, analizamos un caso de uso para demostrar cómo estructurar una app con los componentes de la arquitectura.

Nota: Es imposible que una forma de escribir el código de las apps sea la mejor para cada situación. Ahora bien, esta arquitectura recomendada es un buen punto de partida para la mayoría de las situaciones y los flujos de trabajo. Si ya tienes una manera efectiva de escribir el código de tus apps de Android que cumple con los principios arquitectónicos comunes, no necesitas cambiarla.
Imagina que estamos creando una IU que muestra el perfil de un usuario. Usamos un backend privado y una API de REST para obtener los datos de un perfil determinado.

Resumen
Para comenzar, observa el siguiente diagrama, que muestra cómo todos los módulos deberían interactuar entre sí una vez diseñada la app:



Observa que cada componente solo depende del componente que está un nivel más abajo. Por ejemplo, las actividades y los fragmentos solo dependen de un modelo de vista. El repositorio es la única clase que depende de varias clases. En este ejemplo, el repositorio depende de un modelo de datos persistente y una fuente de datos de backend remota.

Este diseño crea una experiencia del usuario consistente y agradable. Independientemente de que el usuario vuelva a la app varios minutos después de cerrarla por última vez o varios días más tarde, verá al instante la información del usuario de que la app persiste a nivel local. Si estos datos están inactivos, el módulo de repositorio de la app comienza a actualizar los datos en segundo plano.

Cómo crear la interfaz de usuario
La IU consta de un fragmento, UserProfileFragment, y su archivo de diseño correspondiente user_profile_layout.xml.

Para controlar la IU, nuestro modelo de datos necesita retener los siguientes elementos de datos:

ID de usuario: El identificador del usuario. Conviene trasladar esta información al fragmento mediante los argumentos de fragmento. Si el SO Android destruye nuestros procesos, esta información se conservará para que el ID esté disponible la próxima vez que se reinicie nuestra app.
Objeto del usuario: Una clase de datos que retiene información sobre el usuario.
Usamos un UserProfileViewModel, basado en el componente de arquitectura de ViewModel para mantener esta información.

Un objeto de ViewModel proporciona los datos para un componente de IU específico, como un fragmento o una actividad, e incluye lógica empresarial de manejo de datos para comunicarse con el modelo. Por ejemplo, el ViewModel puede llamar a otros componentes para cargar los datos, y puede desviar solicitudes de usuarios para modificar los datos. El ViewModel no tiene conocimiento sobre los componentes de IU, de manera que no se ve afectado por los cambios de configuración, como la recreación de una actividad debido a la rotación del dispositivo.

Ya definimos los siguientes archivos:

user_profile.xml: La definición de diseño de IU para la pantalla.
UserProfileFragment: El controlador de IU que controla los datos.
UserProfileViewModel: La clase que prepara los datos para su visualización en el UserProfileFragment y reacciona a las interacciones del usuario.
Los siguientes fragmentos de código muestran el contenido de inicio de estos archivos. (El archivo de diseño se omite para mayor simplicidad).

UserProfileViewModel

public class UserProfileViewModel extends ViewModel {
    private String userId;
    private User user;

    public void init(String userId) {
        this.userId = userId;
    }
    public User getUser() {
        return user;
    }
}

UserProfileFragment

public class UserProfileFragment extends Fragment {
    private static final String UID_KEY = "uid";
    private UserProfileViewModel viewModel;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        String userId = getArguments().getString(UID_KEY);
        viewModel = ViewModelProviders.of(this).get(UserProfileViewModel.class);
        viewModel.init(userId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                @Nullable ViewGroup container,
                @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_profile, container, false);
    }
}

Ahora que tenemos estos módulos de código, ¿cómo los conectamos? Después de todo, cuando el campo user está establecido en la clase UserProfileViewModel, necesitamos una manera de informar la IU. Aquí es donde interviene el componente de la arquitectura de LiveData.

LiveData es una clase que retiene datos observables. Otros componentes en tu app pueden supervisar cambios en objetos que usan este titular > sin crear rutas de dependencia explícitas y rígidas entre ellos. El componente de LiveData también respeta el estado del ciclo de vida de los componentes de tu app, como las actividades, los fragmentos y los servicios, e incluye lógica de limpieza para evitar las fugas de objetos y el consumo de memoria excesivo.

Nota: Si ya usaste una biblioteca como RxJava o Agera, puedes seguir usándolas en lugar de usar LiveData. Sin embargo, cuando usas bibliotecas y enfoques como estos, asegúrate de administrar correctamente el ciclo de vida de tu app. En especial, asegúrate de pausar tus flujos de datos cuando el LifecycleOwner relacionado esté detenido y de destruir estos flujos cuando se destruya el LifecycleOwner relacionado. También puedes agregar el artefacto android.arch.lifecycle:reactivestreams para usar LiveData con otra biblioteca de flujos reactivos, como RxJava2.
Para incorporar el componente de LiveData a nuestra app, cambia el tipo de campo en el UserProfileViewModel a LiveData<User>. Ahora, el UserProfileFragment recibe una notificación cuando se actualizan los datos. Además, debido a que este campo de LiveData está optimizado para los ciclos de vida, las referencias se borran automáticamente cuando ya no son necesarias.

UserProfileViewModel

public class UserProfileViewModel extends ViewModel {
    ...
    private User user;
    private LiveData<User> user;
    public LiveData<User> getUser() {
        return user;
    }
}

Ahora modificamos UserProfileFragment para observar los datos y actualizar la IU.

UserProfileFragment

@Override
public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    viewModel.getUser().observe(this, user -> {
      // Update UI.
    });
}

Cada vez que se actualicen los datos del usuario, se invocará la devolución de llamada onChanged() y se actualizará la IU.

Si estás familiarizado con otras bibliotecas en las que se usan devoluciones de llamadas, quizás hayas notado que no necesitamos anular el método onStop() del fragmento para dejar de observar los datos. Este paso no es necesario con LiveData porque está optimizado para los ciclos de vida, lo que significa que no invocará la devolución de llamada de onChanged() a menos que un fragmento esté en el estado activo (se recibió onStart(), pero todavía no se recibió onStop()). LiveData también quita automáticamente el observador cuando se llama al método onDestroy() del fragmento.

Tampoco agregamos lógica para manejar los cambios de configuración, como un usuario que rota la pantalla del dispositivo. El UserProfileViewModel se restaura automáticamente cuando la configuración cambia. Por lo tanto, el nuevo fragmento recibirá la misma instancia de ViewModel apenas esté creado y se invocará inmediatamente la devolución de llamada con los datos actuales. Dado que se espera que los objetos ViewModel duren más que los objetos View correspondientes que actualizan, no debes incluir referencias directas a objetos View dentro de tu implementación de ViewModel. Para obtener más información sobre cómo el ciclo de vida de un ViewModel se relaciona con el ciclo de vida de los componentes de IU, consulta El ciclo de vida de un ViewModel.

Cómo obtener datos
Ahora que usamos LiveData para conectar el UserProfileViewModel al UserProfileFragment, ¿cómo podemos obtener los datos del perfil de usuario?

En este ejemplo, suponemos que nuestro backend proporciona una API de REST. Usaremos la biblioteca de Retrofit para acceder a nuestro backend, aunque puedes usar una biblioteca diferente que sirva para el mismo objetivo.

Esta es nuestra definición de Webservice que se comunica con nuestro backend:

Webservice

public interface Webservice {
    /**
     * @GET declares an HTTP GET request
     * @Path("user") annotation on the userId parameter marks it as a
     * replacement for the {user} placeholder in the @GET path
     */
    @GET("/users/{user}")
    Call<User> getUser(@Path("user") String userId);
}

Una idea inicial para implementar ViewModel podría consistir en llamar directamente a Webservice para obtener los datos y asignarlos a nuestro objeto LiveData. Aunque este método funciona, el mantenimiento de nuestra app se complica a medida que esta crece. Asigna demasiada responsabilidad a la clase UserProfileViewModel, lo que infringe el principio de separación de problemas. Además, el alcance de un ViewModel está vinculado al ciclo de vida de una Activity o un Fragment, lo que implica que los datos del Webservice se pierden cuando finaliza el ciclo de vida del objeto de IU asociado. Este comportamiento crea una experiencia del usuario indeseable.

En cambio, nuestro ViewModel delega el proceso de obtención de datos a un nuevo módulo, un repositorio.

Los módulos de repositorio manejan las operaciones de datos. Proporcionan una API limpia para que el resto de la app pueda recuperar estos datos fácilmente. Saben de dónde obtener los datos y qué llamadas de API deben hacer cuando se actualizan los datos. Puedes considerar a los repositorios como mediadores entre diferentes fuentes de datos, como modelos persistentes, servicios web y memorias caché.

Nuestra clase UserRepository, que se muestra en el siguiente fragmento de código, usa una instancia de WebService para obtener los datos de un usuario:

UserRepository

public class UserRepository {
    private Webservice webservice;
    // ...
    public LiveData<User> getUser(int userId) {
        // This isn't an optimal implementation. We'll fix it later.
        final MutableLiveData<User> data = new MutableLiveData<>();
        webservice.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                data.setValue(response.body());
            }

            // Error case is left out for brevity.
        });
        return data;
    }
}

Aunque el módulo de repositorio parece innecesario, tiene un objetivo fundamental: abstrae las fuentes de datos del resto de la app. Ahora bien, nuestro UserProfileViewModel no sabe cómo se obtienen los datos, de manera que podemos proporcionarle datos obtenidos de varias implementaciones de obtención de datos diferentes.

Nota: Omitimos el caso de error de la red por cuestiones de simplicidad. Si quieres conocer una implementación alternativa que expone los errores y estados de carga, consulta Anexo: Cómo exponer el estado de la red.
Cómo administrar dependencias entre los componentes
La clase UserRepository de arriba necesita una instancia de Webservice para obtener los datos del usuario. Podría simplemente crearla, pero para hacerlo, también necesitaría conocer las dependencias de la clase Webservice. Además, es probable que UserRepository no sea la única clase que necesite Webservice. Esta situación nos obliga a duplicar el código, ya que cada clase que necesita una referencia a Webservice debe saber cómo construirla con sus dependencias. Si cada clase crea un WebService nuevo, nuestra app tendría un alto consumo de recursos.

Puedes usar los siguientes patrones de diseño para solucionar este problema:

Inyección de dependencia (DI): La inyección de dependencia permite que las clases definan sus dependencias sin construirlas. En el tiempo de ejecución, otra clase es responsable de proporcionar estas dependencias. Recomendamos la biblioteca Dagger 2 de Google para implementar la inyección de dependencia en las apps de Android. Para construir objetos automáticamente, Dagger 2 recorre el árbol de dependencias y proporciona garantías de tiempo de compilación para las dependencias.
Localizador de servicios: El localizador de servicios brinda un registro en el que las clases pueden obtener sus dependencias en lugar de construirlas.
Es más fácil implementar un registro de servicio que usar la inyección de dependencia (DI), de manera que, si no estás familiarizado con DI, recomendamos usar el patrón del localizador de servicios en su lugar.

Estos patrones te permiten hacer un escalamiento del código, ya que proporcionan patrones claros para administrar dependencias sin duplicar el código ni aumentar la complejidad. Además, estos patrones te permiten cambiar rápidamente entre las implementaciones de obtención de datos de producción y de prueba.

Nuestra app de ejemplo usa Dagger 2 para administrar las dependencias del objeto Webservice.

Cómo conectar ViewModel y el repositorio
Ahora, modificamos nuestro UserProfileViewModel para usar el objeto UserRepository:

UserProfileViewModel

public class UserProfileViewModel extends ViewModel {
    private LiveData<User> user;
    private UserRepository userRepo;

    // Instructs Dagger 2 to provide the UserRepository parameter.
    @Inject
    public UserProfileViewModel(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public void init(int userId) {
        if (this.user != null) {
            // ViewModel is created on a per-Fragment basis, so the userId
            // doesn't change.
            return;
        }
        user = userRepo.getUser(userId);
    }

    public LiveData<User> getUser() {
        return this.user;
    }
}

Datos de caché
La implementación de UserRepository abstrae la llamada al objeto Webservice, pero no es muy flexible, ya que solo depende de una fuente de datos.

El problema clave con la implementación de UserRepository es que después de obtener los datos de nuestro backend, no los almacena en ningún lugar. Por lo tanto, si el usuario abandona el UserProfileFragment y más tarde regresa a él, nuestra app debe volver a obtener los datos, incluso si no cambiaron.

Este diseño es subóptimo debido a los siguientes motivos:

Desperdicia ancho de banda de la red.
Obliga al usuario a esperar que se complete la consulta nueva.
Para solucionar este problema, agregamos una nueva fuente de datos a nuestro UserRepository, que almacena los objetos User en la memoria caché:

UserRepository

// Informs Dagger that this class should be constructed only once.
@Singleton
public class UserRepository {
    private Webservice webservice;

    // Simple in-memory cache. Details omitted for brevity.
    private UserCache userCache;

    public LiveData<User> getUser(int userId) {
        LiveData<User> cached = userCache.get(userId);
        if (cached != null) {
            return cached;
        }

        final MutableLiveData<User> data = new MutableLiveData<>();
        userCache.put(userId, data);

        // This implementation is still suboptimal but better than before.
        // A complete implementation also handles error cases.
        webservice.getUser(userId).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                data.setValue(response.body());
            }
        });
        return data;
    }
}

Cómo conservar los datos
Con nuestra implementación actual, si el usuario rota el dispositivo o abandona la app y vuelve a ella inmediatamente, la IU existente estará visible instantáneamente porque el repositorio recupera los datos de la memoria caché integrada.

Sin embargo, ¿qué sucede si el usuario abandona la app y vuelve a ella horas más tarde, cuando el SO Android ya finalizó el proceso? Con la implementación actual, necesitaremos volver a obtener los datos de la red. Esto no solo representa una mala experiencia del usuario, sino que también es un desperdicio, ya que consume valiosos datos móviles.

Para solucionar este problema puedes almacenar las solicitudes web en la memoria caché, pero esto crea un nuevo problema: ¿Qué sucede si a partir de otro tipo de solicitud, como obtener una lista de amigos, se muestran los mismos datos del usuario? La app mostraría datos inconsistentes, lo que crearía una confusión en el mejor de los casos. Por ejemplo, nuestra app podría mostrar dos versiones diferentes de los mismos datos del usuario si el usuario hizo la solicitud de lista de amigos y la solicitud de un único usuario en momentos diferentes. Nuestra app tendría que averiguar cómo fusionar estos datos inconsistentes.

La manera correcta de manejar esta situación es usar un modelo persistente. En estos casos, interviene la biblioteca de persistencias de Room.

Room es una biblioteca de asignación de objetos que ofrece persistencia de datos local con la cantidad mínima de código estándar. En el momento de la compilación, valida cada consulta respecto del esquema de datos, de manera que las consultas de SQL rotas ocasionan errores de tiempo de compilación en lugar de fallas de tiempo de ejecución. Room abstrae algunos de los detalles de implementación subyacentes por el trabajo con tablas y consultas SQL sin procesar. Además, permite observar cambios en la base de datos (incluidas colecciones y solicitudes para unirse), lo que expone estos cambios mediante los objetos LiveData. Además, define de manera explícita restricciones de subproceso que abordan problemas comunes, como acceder al almacenamiento en el subproceso principal.

Nota: Si tu app ya usa otra solución de persistencia, como una asignación relacional de objeto (ORM) de SQLite, no necesitas reemplazar tu solución actual por Room. Sin embargo, si escribes el código de una app nueva o si modificas una app ya existente, recomendamos usar Room para la persistencia de los datos de tu app. De esa manera, puedes aprovechar las capacidades de abstracción y validación de consultas de la biblioteca.
Para usar Room, necesitamos definir nuestro esquema local. Primero, agregamos la anotación @Entity a nuestra clase de modelo de datos de User y una anotación @PrimaryKey al campo id de la clase. Estas anotaciones identifican a User como una tabla en nuestra base de datos y a id como la clave principal de la tabla:

Usuario

@Entity
class User {
  @PrimaryKey
  private int id;
  private String name;
  private String lastName;

  // Getters and setters for fields.
}

Luego, creamos una clase de base de datos al implementar RoomDatabase para nuestra app:

UserDatabase

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
}

Ten en cuenta que UserDatabase es abstracto. Room proporciona una implementación de la base de datos automáticamente. Consulta la documentación de Room para obtener más detalles.

Ahora necesitamos una manera de insertar los datos del usuario en la base de datos. Para esto, necesitaremos crear un objeto de acceso de datos (DAO).

UserDao

@Dao
public interface UserDao {
    @Insert(onConflict = REPLACE)
    void save(User user);
    @Query("SELECT * FROM user WHERE id = :userId")
    LiveData<User> load(int userId);
}

Ten en cuenta que el método load muestra LiveData<User>. Room sabe cuándo se modifica la base de datos y notifica automáticamente a todos los observadores activos cuando los datos cambian. Debido a que Room usa LiveData, la operación es efectiva, ya que actualiza los datos solo si hay un observador activo como mínimo.

Nota: Room verifica que no haya invalidaciones según las modificaciones de tabla, de manera que podría despachar notificaciones que sean falsos positivos.
Una vez definida la clase UserDao, hacemos una referencia al DAO a partir de nuestra clase de base de datos:

UserDatabase

@Database(entities = {User.class}, version = 1)
public abstract class UserDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}

Ahora podemos modificar nuestro UserRepository para incorporar la fuente de datos de Room:

@Singleton
public class UserRepository {
    private final Webservice webservice;
    private final UserDao userDao;
    private final Executor executor;

    @Inject
    public UserRepository(Webservice webservice, UserDao userDao, Executor executor) {
        this.webservice = webservice;
        this.userDao = userDao;
        this.executor = executor;
    }

    public LiveData<User> getUser(String userId) {
        refreshUser(userId);
        // Returns a LiveData object directly from the database.
        return userDao.load(userId);
    }

    private void refreshUser(final String userId) {
        // Runs in a background thread.
        executor.execute(() -> {
            // Check if user data was fetched recently.
            boolean userExists = userDao.hasUser(FRESH_TIMEOUT);
            if (!userExists) {
                // Refreshes the data.
                Response<User> response = webservice.getUser(userId).execute();

                // Check for errors here.

                // Updates the database. The LiveData object automatically
                // refreshes, so we don't need to do anything else here.
                userDao.save(response.body());
            }
        });
    }
}

Ten en cuenta que, aunque cambiamos el origen de los datos en UserRepository, no tuvimos que cambiar nuestro UserProfileViewModel ni UserProfileFragment. Esta actualización de alcance limitado demuestra la flexibilidad que ofrece la arquitectura de nuestra app. También es excelente para las pruebas, ya que podemos proporcionar un UserRepository falso y probar nuestro UserProfileViewModel de producción al mismo tiempo.

Si pasan algunos días hasta que un usuario vuelve a una app que usa esta arquitectura, es probable que vea información desactualizada hasta que el repositorio pueda obtener información actualizada. Según tu caso de uso, quizás prefieras no mostrar esta información desactualizada. En su lugar, puedes mostrar datos del marcador de posición, que proporcionan valores ficticios e indican que tu app está actualmente buscando y cargando información actualizada.

Fuente de confianza única
Es común que varios extremos de la API de REST muestren los mismos datos. Por ejemplo, si nuestro backend tiene otro extremo que muestra una lista de amigos, el mismo objeto de usuario podría provenir de dos extremos de la API diferentes, quizás con niveles de detalle diferentes. Si el UserRepository muestra la respuesta de la solicitud Webservice sin modificaciones y sin verificar la coherencia, nuestras IU podrían mostrar información confusa, ya que la versión y el formato de los datos del repositorio dependerían del extremo al que se llamó por última vez.

Por este motivo, nuestra implementación de UserRepository guarda las respuestas del servicio web en la base de datos. Luego, los cambios en la base de datos activan llamadas de devolución en los objetos LiveData activos. Con este modelo, la base de datos funciona como la única fuente de confianza y otras partes de la app acceden a ella a través de nuestro UserRepository. Independientemente de que uses una caché de disco o no, recomendamos que tu repositorio designe una fuente de datos como fuente de confianza única para el resto de tu app.

Cómo mostrar las operaciones en curso
En algunos casos de uso, como "deslizar hacia abajo para actualizar", es importante que la IU le muestre al usuario si hay una operación de red en curso. Recomendamos separar la acción de IU de los datos reales, ya que podrían actualizarse por varios motivos. Por ejemplo, si obtenemos una lista de amigos, podría obtenerse el mismo usuario de forma programática, lo cual activaría una actualización de LiveData<User>. Desde el punto de vista de la IU, el hecho de que haya una solicitud en curso es solamente otro punto de datos, similar a cualquier otro dato de pieza (como el objeto User).

Podemos usar una de las siguientes estrategias para mostrar un estado de actualización de datos coherente en la IU, independientemente del lugar de donde provino la solicitud para actualizar los datos.

Cambia getUser() para que muestre un objeto de tipo LiveData. Este objeto incluirá el estado de operación de la red.

Por ejemplo, consulta la implementación de NetworkBoundResource en el proyecto de GitHub sobre componentes de la arquitectura de Android.

Proporciona otra función pública en la clase de UserRepository que pueda mostrar el estado actualizado del User. Esta opción es mejor si quieres mostrar el estado de red en tu IU solo cuando el proceso de obtención de datos se originó como respuesta a una acción de usuario explícita (como "deslizar hacia abajo para actualizar").

Cómo probar cada componente
En la sección de separación de problemas, mencionamos que uno de los beneficios clave de usar este principio es la capacidad de prueba.

La siguiente lista muestra cómo probar cada módulo de código de nuestro ejemplo extendido:

Interfaz de usuario e interacciones: Usa una prueba de instrumentación de IU de Android. La mejor manera de crear esta prueba es usar la biblioteca Espresso. Puedes crear el fragmento y proporcionarle un UserProfileViewModel ficticio. Debido a que el fragmento solo se comunica con el UserProfileViewModel, crear esta clase ficticia es suficiente para probar la IU de tu app por completo.

ViewModel: Puedes probar la clase de UserProfileViewModel con una prueba JUnit. Solo necesitas crear una versión ficticia de una clase, UserRepository.

UserRepository: También puedes probar UserRepository con una prueba JUnit. Necesitas crear una versión ficticia de Webservice y UserDao. En estas pruebas, verifica que ocurra lo siguiente:

El repositorio hace las llamadas de servicio web correctas.
El repositorio guarda los resultados en la base de datos.
El repositorio no hace solicitudes innecesarias si los datos están en la memoria caché y actualizados
Debido a que tanto Webservice como UserDao son interfaces, puedes crear versiones ficticias de ellas o implementaciones falsas para casos de prueba más complejos.

UserDao: Prueba las clases DAO con pruebas de instrumentación. Estas pruebas de implementación no requieren componentes de IU, ya que se ejecutan rápidamente.

Para cada prueba, crea una base de datos en la memoria a fin de garantizar que la prueba no tenga efectos secundarios (como cambiar los archivos de base de datos en el disco).

Precaución: Room también permite especificar la implementación de la base de datos para que puedas probar tu DAO al proporcionarle la implementación de JUnit de SupportSQLiteOpenHelper. Sin embargo, este enfoque no se recomienda, ya que la versión de SQLite que se ejecuta en el dispositivo podría diferir de la versión de SQLite en tu máquina de desarrollo.

Webservice: En estas pruebas, evita hacer llamadas de red a tu backend. Es importante para todas las pruebas, especialmente para las basadas en la Web, a fin de que sean independientes del mundo exterior.

Varias bibliotecas, como MockWebServer, pueden ayudarte a crear un servidor local falso para estas pruebas.

Probar artefactos: Los componentes de la arquitectura proporcionan un artefacto Maven para controlar sus subprocesos en segundo plano. El artefacto android.arch.core:core-testing incluye las siguientes reglas de JUnit:

InstantTaskExecutorRule: Usa esta regla para ejecutar de manera instantánea cualquier operación en segundo plano en el subproceso de llamada.
CountingTaskExecutorRule: Usa esta regla para esperar las operaciones en segundo plano de los componentes de la arquitectura. También puedes asociar esta regla con Espresso como un recurso inactivo.
Prácticas recomendadas
La programación es una disciplina creativa y crear apps de Android no es una excepción. Hay muchas maneras de solucionar un problema: comunicar datos entre varias actividades o fragmentos, recuperar datos remotos y conservarlos a nivel local para el modo sin conexión o varias otras situaciones frecuentes con las que pueden encontrarse las apps no triviales.

Aunque las siguientes recomendaciones no son obligatorias, nuestra experiencia nos demuestra que, si las sigues, tu código base será más confiable, tendrá mayor capacidad de prueba y será más fácil de mantener a largo plazo.

Evita designar los puntos de entrada de tu app (receptores de transmisiones, servicios y actividades), como fuentes de datos.
En cambio, solo deben coordinar con otros componentes para recuperar el subconjunto de datos relevante para ese punto de entrada. Cada componente de la app tiene una duración relativamente corta, según la interacción que el usuario tenga con su dispositivo y el estado actual general del sistema.
Crea límites de responsabilidad bien definidos entre varios módulos de tu app.
Por ejemplo, no extiendas el código que carga datos de la red entre varias clases o paquetes en tu código base. Del mismo modo, no definas varias responsabilidades no relacionadas, como caché de datos y vinculación de datos, en la misma clase.
Expón tan poco como sea posible de cada módulo.
No caigas en la tentación de crear accesos directos que expongan detalles internos de la implementación de un módulo. Quizás ahorres algo de tiempo a corto plazo, pero tendrás muchos problemas técnicos a medida que tu código base evolucione.
Piensa en cómo lograr que cada módulo se pueda probar por separado.
Por ejemplo, una API bien definida para obtener datos de la red facilitará las pruebas que realices en el módulo que conserve esos datos en la base de datos local. En cambio, si combinas la lógica de estos dos módulos en un solo lugar, o si distribuyes el código de red por todo tu código base, será mucho más difícil (y quizás hasta imposible) ponerlo a prueba.
Concéntrate en aquello que hace única a tu app para que se destaque del resto.
No desperdicies tu tiempo reinventando algo que ya existe ni escribiendo el mismo código estándar una y otra vez. En cambio, enfoca tu tiempo y tu energía en aquello que hace que tu app sea única y deja que los componentes de la arquitectura de Android y otras bibliotecas recomendadas se ocupen del código estándar repetitivo.
Conserva la mayor cantidad posible de datos relevantes y actualizados.
De esa manera, los usuarios puedes aprovechar la funcionalidad de tu app, incluso cuando su dispositivo está en modo sin conexión. Recuerda que no todos tus usuarios cuentan con una conexión de alta velocidad de manera constante.
Asigna una fuente de datos como la única fuente de confianza.
Cada vez que tu app necesite acceso a estos datos, debería originarse a partir de una fuente de confianza única.
Anexo: Cómo exponer el estado de la red
En la sección sobre arquitectura de la app recomendada anterior, omitimos intencionalmente los errores de red y los estados de carga para mantener los fragmentos de código simples.

En esta sección, se muestra cómo exponer el estado de la red mediante una clase Resource que encapsula tanto los datos como su estado.

El siguiente fragmento de código proporciona una implementación de muestra de Resource:

// A generic class that contains data and status about loading this data.
public class Resource<T> {
    @NonNull public final Status status;
    @Nullable public final T data;
    @Nullable public final String message;
    private Resource(@NonNull Status status, @Nullable T data,
            @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@NonNull T data) {
        return new Resource<>(Status.SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg, @Nullable T data) {
        return new Resource<>(Status.ERROR, data, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(Status.LOADING, data, null);
    }

    public enum Status { SUCCESS, ERROR, LOADING }
}

Debido a que es común cargar los datos desde la red al mismo tiempo que se muestra la copia en el disco de esos datos, recomendamos crear una clase de ayuda que puedas volver a usar en varios lugares. Para este ejemplo, creamos una clase llamada NetworkBoundResource.

El siguiente diagrama muestra el árbol de decisión de NetworkBoundResource:



Para comenzar, observa la base de datos del recurso. Una vez que la entrada está cargada desde la base de datos por primera vez, NetworkBoundResource verifica si el resultado es suficientemente bueno para despacharlo o si debería volver a obtenerlo de la red. Ten en cuenta que los dos casos pueden suceder al mismo tiempo, ya que probablemente quieras mostrar los datos en la memoria caché al mismo tiempo que los actualizas desde la red.

Si la llamada de red se completa correctamente, el tiempo de respuesta se guarda en la base de datos y se vuelve a inicializar el flujo. Si la solicitud de red falla, NetworkBoundResource despacha una falla directamente.

Nota: Después de guardar datos nuevos en el disco, volvemos a inicializar el flujo desde la base de datos. Sin embargo, generalmente no es necesario hacerlo, ya que la base de datos despachará el cambio.

Ten en cuenta que confiar en la base de datos para que despache el cambio implica confiar en los efectos secundarios asociados, lo cual no es recomendable, ya que podría ocurrir un comportamiento indefinido si la base de datos termina sin despachar los cambios debido a que los datos no cambiaron.

Tampoco es recomendable despachar el resultado que se obtuvo de la red, ya que eso infringiría el principio de fuente de confianza única. Después de todo, quizás la base de datos incluye activadores que cambian los valores de los datos durante una operación de "guardar". Del mismo modo, no despaches SUCCESS sin los datos nuevos, ya que el cliente recibiría la versión incorrecta de los datos.

El siguiente fragmento de código muestra la API púbica que proporciona la clase NetworkBoundResource para sus elementos secundarios.

// ResultType: Type for the Resource data.
// RequestType: Type for the API response.
public abstract class NetworkBoundResource<ResultType, RequestType> {
    // Called to save the result of the API response into the database.
    @WorkerThread
    protected abstract void saveCallResult(@NonNull RequestType item);

    // Called with the data in the database to decide whether to fetch
    // potentially updated data from the network.
    @MainThread
    protected abstract boolean shouldFetch(@Nullable ResultType data);

    // Called to get the cached data from the database.
    @NonNull @MainThread
    protected abstract LiveData<ResultType> loadFromDb();

    // Called to create the API call.
    @NonNull @MainThread
    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    // Called when the fetch fails. The child class may want to reset components
    // like rate limiter.
    @MainThread
    protected void onFetchFailed();

    // Returns a LiveData object that represents the resource that's implemented
    // in the base class.
    public final LiveData<Resource<ResultType>> getAsLiveData();
}

Ten en cuenta esta información importante sobre la definición de la clase:

Define dos tipos de parámetros, ResultType y RequestType, ya que el tipo de datos que mostró la API podría no coincidir con el tipo de datos que se usa a nivel local.
Usa una clase llamada ApiResponse para las solicitudes de red. ApiResponse es un wrapper sencillo alrededor de la clase Retrofit2.Call que convierte las respuestas en instancias de LiveData.
La implementación completa de la clase NetworkBoundResource se muestra como parte del proyecto de GitHub sobre componentes de la arquitectura de Android.

Después de crear NetworkBoundResource, podemos usarlo para escribir el código de nuestras implementaciones User de disco y vinculadas a la red en la clase UserRepository:

UserRepository

class UserRepository {
    Webservice webservice;
    UserDao userDao;

    public LiveData<Resource<User>> loadUser(final int userId) {
        return new NetworkBoundResource<User,User>() {
            @Override
            protected void saveCallResult(@NonNull User item) {
                userDao.insert(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable User data) {
                return rateLimiter.canFetch(userId)
                        && (data == null || !isFresh(data));
            }

            @NonNull @Override
            protected LiveData<User> loadFromDb() {
                return userDao.load(userId);
            }

            @NonNull @Override
            protected LiveData<ApiResponse<User>> createCall() {
                return webservice.getUser(userId);
            }
        }.getAsLiveData();
    }
}
