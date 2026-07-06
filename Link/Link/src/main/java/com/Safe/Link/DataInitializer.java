package com.Safe.Link;

import com.Safe.Link.entities.ConsejoPreventivo;
import com.Safe.Link.entities.Familia;
import com.Safe.Link.entities.FaqPregunta;
import com.Safe.Link.entities.ItemKit;
import com.Safe.Link.entities.KitEmergencia;
import com.Safe.Link.entities.Miembros_de_familia;
import com.Safe.Link.entities.NumeroEmergencia;
import com.Safe.Link.entities.PuntoSeguro;
import com.Safe.Link.entities.Refugio;
import com.Safe.Link.entities.Usuario;
import com.Safe.Link.repositories.ConsejoPreventivoRepository;
import com.Safe.Link.repositories.FamiliaRepository;
import com.Safe.Link.repositories.FaqPreguntaRepository;
import com.Safe.Link.repositories.ItemKitRepository;
import com.Safe.Link.repositories.KitEmergenciaRepository;
import com.Safe.Link.repositories.Miembros_de_familia_repository;
import com.Safe.Link.repositories.NumeroEmergenciaRepository;
import com.Safe.Link.repositories.PuntoSeguroRepository;
import com.Safe.Link.repositories.RefugioRepository;
import com.Safe.Link.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private static final String DEMO_EMAIL = "demo@safelink.com";

    private final ConsejoPreventivoRepository consejoRepository;
    private final FaqPreguntaRepository faqRepository;
    private final PuntoSeguroRepository puntoSeguroRepository;
    private final RefugioRepository refugioRepository;
    private final NumeroEmergenciaRepository numeroEmergenciaRepository;
    private final UsuarioRepository usuarioRepository;
    private final KitEmergenciaRepository kitRepository;
    private final ItemKitRepository itemKitRepository;
    private final FamiliaRepository familiaRepository;
    private final Miembros_de_familia_repository miembroRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        seedConsejos();
        seedFaq();
        seedPuntosSeguros();
        seedRefugios();
        seedNumerosEmergencia();

        Usuario demo = seedDemoUser();
        asegurarAdminUnico(demo);
        seedDemoKit(demo);
        seedDemoFamilia(demo);
    }

    private void seedConsejos() {
        List<ConsejoPreventivo> existentes = consejoRepository.findAll();
        List<ConsejoPreventivo> nuevos = new ArrayList<>();

        addConsejoIfMissing(nuevos, existentes,
                "Prepara una mochila de emergencia con agua, alimentos no perecibles, linterna y radio.",
                "Pack an emergency bag with water, non-perishable food, a flashlight, and a radio.",
                "sismo", "preparacion", 1);
        addConsejoIfMissing(nuevos, existentes,
                "Identifica las zonas seguras internas y externas de tu vivienda antes de una emergencia.",
                "Identify the safe indoor and outdoor areas of your home before an emergency.",
                "sismo", "prevencion", 2);
        addConsejoIfMissing(nuevos, existentes,
                "Mantente alejado de ventanas, postes y cables electricos durante una evacuacion.",
                "Stay away from windows, poles, and power lines during an evacuation.",
                "sismo", "evacuacion", 3);
        addConsejoIfMissing(nuevos, existentes,
                "Acuerda con tu familia un punto de encuentro y una forma de comunicarse si se separan.",
                "Agree on a meeting point with your family and a way to communicate if you get separated.",
                "general", "familia", 4);
        addConsejoIfMissing(nuevos, existentes,
                "Ten una copia digital de tus documentos importantes y contactos de emergencia.",
                "Keep a digital copy of your important documents and emergency contacts.",
                "general", "documentos", 5);
        addConsejoIfMissing(nuevos, existentes,
                "Si tiembla, agachate, cubrete y sujetate hasta que termine el movimiento.",
                "If it shakes, drop, cover, and hold on until the shaking stops.",
                "sismo", "respuesta", 6);
        addConsejoIfMissing(nuevos, existentes,
                "Revisa cada mes las fechas de vencimiento de tu agua, alimentos y medicinas.",
                "Check the expiration dates of your water, food, and medicine every month.",
                "general", "kit", 7);
        addConsejoIfMissing(nuevos, existentes,
                "Despues de evacuar, no regreses a casa hasta que la zona sea declarada segura.",
                "After evacuating, don't return home until the area is declared safe.",
                "sismo", "post-emergencia", 8);

        if (!nuevos.isEmpty()) {
            consejoRepository.saveAll(nuevos);
        }

        List<ConsejoPreventivo> pendientesDeTraduccion = existentes.stream()
                .filter(c -> c.getContenidoEn() == null || c.getContenidoEn().isBlank())
                .toList();
        if (!pendientesDeTraduccion.isEmpty()) {
            pendientesDeTraduccion.forEach(c -> c.setContenidoEn(traduccionConocida(c.getContenido())));
            consejoRepository.saveAll(pendientesDeTraduccion);
        }
    }

    private String traduccionConocida(String contenidoEs) {
        return switch (contenidoEs) {
            case "Prepara una mochila de emergencia con agua, alimentos no perecibles, linterna y radio." ->
                    "Pack an emergency bag with water, non-perishable food, a flashlight, and a radio.";
            case "Identifica las zonas seguras internas y externas de tu vivienda antes de una emergencia." ->
                    "Identify the safe indoor and outdoor areas of your home before an emergency.";
            case "Mantente alejado de ventanas, postes y cables electricos durante una evacuacion." ->
                    "Stay away from windows, poles, and power lines during an evacuation.";
            case "Acuerda con tu familia un punto de encuentro y una forma de comunicarse si se separan." ->
                    "Agree on a meeting point with your family and a way to communicate if you get separated.";
            case "Ten una copia digital de tus documentos importantes y contactos de emergencia." ->
                    "Keep a digital copy of your important documents and emergency contacts.";
            case "Si tiembla, agachate, cubrete y sujetate hasta que termine el movimiento." ->
                    "If it shakes, drop, cover, and hold on until the shaking stops.";
            case "Revisa cada mes las fechas de vencimiento de tu agua, alimentos y medicinas." ->
                    "Check the expiration dates of your water, food, and medicine every month.";
            case "Despues de evacuar, no regreses a casa hasta que la zona sea declarada segura." ->
                    "After evacuating, don't return home until the area is declared safe.";
            default -> contenidoEs;
        };
    }

    private void seedFaq() {
        List<FaqPregunta> existentes = faqRepository.findAll();
        List<FaqPregunta> nuevas = new ArrayList<>();

        addFaqIfMissing(nuevas, existentes,
                "Que debe tener una mochila de emergencia?",
                "Debe incluir agua, alimentos no perecibles, botiquin, linterna, radio, pilas, documentos y articulos de higiene.",
                "preparacion", 1, "INDECI");
        addFaqIfMissing(nuevas, existentes,
                "Como ubico un punto seguro?",
                "Revisa el mapa de puntos seguros y elige el lugar oficial mas cercano a tu vivienda o centro de trabajo.",
                "mapa", 2, "Municipalidad");
        addFaqIfMissing(nuevas, existentes,
                "Que hago si mi familia no esta reunida?",
                "Usa el punto de encuentro familiar registrado y comunicate por mensajes cortos para evitar saturar la red.",
                "familia", 3, "INDECI");
        addFaqIfMissing(nuevas, existentes,
                "Puedo usar SafeLink sin iniciar sesion?",
                "Si. Puedes consultar consejos, FAQ, puntos seguros, refugios y numeros de emergencia sin iniciar sesion.",
                "app", 4, "SafeLink");
        addFaqIfMissing(nuevas, existentes,
                "Cuando debo ir a un refugio?",
                "Dirigete a un refugio si tu vivienda no es segura, hay danos visibles o la autoridad recomienda evacuar.",
                "refugios", 5, "Defensa Civil");
        addFaqIfMissing(nuevas, existentes,
                "Como preparo a ninos o adultos mayores?",
                "Incluye sus medicinas, documentos, abrigo, agua y un contacto visible dentro del kit familiar.",
                "familia", 6, "INDECI");

        if (!nuevas.isEmpty()) {
            faqRepository.saveAll(nuevas);
        }
    }

    private void seedPuntosSeguros() {
        List<PuntoSeguro> existentes = puntoSeguroRepository.findAll();
        List<PuntoSeguro> nuevos = new ArrayList<>();

        addPuntoSeguroIfMissing(nuevos, existentes, "Plaza Bolognesi - Zona Segura", "zona_segura",
                -12.059950, -77.037500,
                "Plaza Bolognesi, Cercado de Lima. Area abierta; segura por amplitud y rutas visibles.",
                450, true);
        addPuntoSeguroIfMissing(nuevos, existentes, "Parque de la Exposicion", "parque",
                -12.060980, -77.036520,
                "Av. 28 de Julio, Cercado. Parque amplio; seguro por espacios libres y accesos multiples.",
                700, true);
        addPuntoSeguroIfMissing(nuevos, existentes, "Campo de Marte - Punto de Encuentro", "area_abierta",
                -12.067450, -77.043210,
                "Av. de la Peruanidad, Jesus Maria. Explanada amplia; lejos de edificios altos.",
                900, true);
        addPuntoSeguroIfMissing(nuevos, existentes, "Parque Murillo - Brena", "parque",
                -12.059050, -77.050940,
                "Jr. Huaraz, Brena. Referencia barrial abierta; segura por visibilidad y acceso peatonal.",
                250, true);
        addPuntoSeguroIfMissing(nuevos, existentes, "Parque Alberti - Jesus Maria", "parque",
                -12.075130, -77.046090,
                "Av. Horacio Urteaga, Jesus Maria. Area verde abierta; punto claro para reunion familiar.",
                300, true);

        if (!nuevos.isEmpty()) {
            puntoSeguroRepository.saveAll(nuevos);
        }
    }

    private void seedRefugios() {
        List<Refugio> existentes = refugioRepository.findAll();
        List<Refugio> nuevos = new ArrayList<>();

        addRefugioIfMissing(nuevos, existentes, "Refugio Municipal Brena",
                "Jr. General Varela 790, Brena. Seguro por estructura municipal, control de ingreso y area techada.",
                -12.058820, -77.052360, 120, 20, true, "Defensa Civil Brena");
        addRefugioIfMissing(nuevos, existentes, "Centro Comunal Jesus Maria",
                "Av. Horacio Urteaga 535. Seguro por acceso amplio, servicios basicos y supervision municipal.",
                -12.074650, -77.046480, 160, 35, true, "Municipalidad de Jesus Maria");
        addRefugioIfMissing(nuevos, existentes, "Coliseo Cerrado del Cercado",
                "Av. Bolivia 150, Cercado. Seguro por gran capacidad, zona techada y rutas de evacuacion claras.",
                -12.058430, -77.039780, 220, 60, true, "Defensa Civil Lima");

        if (!nuevos.isEmpty()) {
            refugioRepository.saveAll(nuevos);
        }
    }

    private void seedNumerosEmergencia() {
        List<NumeroEmergencia> existentes = numeroEmergenciaRepository.findAll();
        List<NumeroEmergencia> nuevos = new ArrayList<>();

        addNumeroEmergenciaIfMissing(nuevos, existentes, "Policia Nacional", "105",
                "Atencion de emergencias policiales.", "shield", 1);
        addNumeroEmergenciaIfMissing(nuevos, existentes, "Bomberos", "116",
                "Incendios, rescates y emergencias.", "flame", 2);
        addNumeroEmergenciaIfMissing(nuevos, existentes, "SAMU", "106",
                "Emergencias medicas.", "ambulance", 3);

        if (!nuevos.isEmpty()) {
            numeroEmergenciaRepository.saveAll(nuevos);
        }
    }

    private Usuario seedDemoUser() {
        Usuario usuario = usuarioRepository.findByCorreo(DEMO_EMAIL)
                .orElseGet(Usuario::new);

        usuario.setNombre("Usuario");
        usuario.setApellido("Demo");
        usuario.setEdad(30);
        usuario.setCorreo(DEMO_EMAIL);
        usuario.setTelefono("999888777");
        usuario.setTipo_usuario("ADMIN");
        usuario.setDistrito("Brena");
        if (usuario.getFecha_registro() == null) {
            usuario.setFecha_registro(LocalDate.now());
        }
        if (usuario.getContrasena() == null || !passwordEncoder.matches("Demo1234", usuario.getContrasena())) {
            usuario.setContrasena(passwordEncoder.encode("Demo1234"));
        }

        return usuarioRepository.save(usuario);
    }

    private void asegurarAdminUnico(Usuario demo) {
        List<Usuario> adminsNoDemo = usuarioRepository.findByTipoUsuario("ADMIN").stream()
                .filter(usuario -> !DEMO_EMAIL.equalsIgnoreCase(usuario.getCorreo()))
                .toList();

        adminsNoDemo.forEach(usuario -> usuario.setTipo_usuario("RESIDENTE"));
        if (!adminsNoDemo.isEmpty()) {
            usuarioRepository.saveAll(adminsNoDemo);
        }
    }

    private void seedDemoKit(Usuario usuario) {
        KitEmergencia kit = kitRepository.findByUsuarioId(usuario.getId())
                .orElseGet(() -> {
                    KitEmergencia nuevoKit = new KitEmergencia();
                    nuevoKit.setUsuario(usuario);
                    nuevoKit.setFechaUltimaRevision(LocalDate.now());
                    nuevoKit.setPorcentaje(60.0);
                    return kitRepository.save(nuevoKit);
                });

        List<ItemKit> existentes = itemKitRepository.findByKitId(kit.getId());
        List<ItemKit> nuevos = new ArrayList<>();

        addItemKitIfMissing(nuevos, existentes, kit, "Agua embotellada", "Bottled water", "agua", 3, true);
        addItemKitIfMissing(nuevos, existentes, kit, "Botiquin basico", "Basic first aid kit", "medicamento", 1, true);
        addItemKitIfMissing(nuevos, existentes, kit, "Linterna con pilas", "Flashlight with batteries", "herramienta", 1, false);
        addItemKitIfMissing(nuevos, existentes, kit, "Radio portatil", "Portable radio", "comunicacion", 1, false);
        addItemKitIfMissing(nuevos, existentes, kit, "Documentos en bolsa hermetica", "Documents in a sealed bag", "documentos", 1, true);
        addItemKitIfMissing(nuevos, existentes, kit, "Alimentos no perecibles", "Non-perishable food", "alimentos", 5, true);
        addItemKitIfMissing(nuevos, existentes, kit, "Silbato", "Whistle", "herramienta", 1, false);
        addItemKitIfMissing(nuevos, existentes, kit, "Manta termica", "Thermal blanket", "abrigo", 1, false);
        addItemKitIfMissing(nuevos, existentes, kit, "Cargador portatil", "Portable charger", "comunicacion", 1, false);
        addItemKitIfMissing(nuevos, existentes, kit, "Mascarillas", "Face masks", "proteccion", 5, false);

        if (!nuevos.isEmpty()) {
            itemKitRepository.saveAll(nuevos);
        }

        List<ItemKit> itemsPendientesDeTraduccion = existentes.stream()
                .filter(i -> i.getNombreItemEn() == null || i.getNombreItemEn().isBlank())
                .toList();
        if (!itemsPendientesDeTraduccion.isEmpty()) {
            itemsPendientesDeTraduccion.forEach(i -> i.setNombreItemEn(traduccionNombreItem(i.getNombreItem())));
            itemKitRepository.saveAll(itemsPendientesDeTraduccion);
        }
    }

    private String traduccionNombreItem(String nombreEs) {
        return switch (nombreEs) {
            case "Agua embotellada" -> "Bottled water";
            case "Botiquin basico" -> "Basic first aid kit";
            case "Linterna con pilas" -> "Flashlight with batteries";
            case "Radio portatil" -> "Portable radio";
            case "Documentos en bolsa hermetica" -> "Documents in a sealed bag";
            case "Alimentos no perecibles" -> "Non-perishable food";
            case "Silbato" -> "Whistle";
            case "Manta termica" -> "Thermal blanket";
            case "Cargador portatil" -> "Portable charger";
            case "Mascarillas" -> "Face masks";
            default -> nombreEs;
        };
    }

    private void seedDemoFamilia(Usuario usuario) {
        Familia familia = familiaRepository.findByUsuarioId(usuario.getId())
                .orElseGet(() -> {
                    Familia nuevaFamilia = new Familia();
                    nuevaFamilia.setUsuario(usuario);
                    nuevaFamilia.setNombreFamilia("Familia Demo");
                    nuevaFamilia.setPuntoEncuentro("Parque Central Seguro");
                    return familiaRepository.save(nuevaFamilia);
                });

        familia.setNombreFamilia("Familia Demo");
        familia.setPuntoEncuentro("Campo de Marte - Punto de Encuentro");
        familiaRepository.save(familia);

        List<Miembros_de_familia> existentes = miembroRepository.findByIdFamilia(familia.getId());
        List<Miembros_de_familia> nuevos = new ArrayList<>();

        addMiembroIfMissing(nuevos, existentes, familia.getId(), "Ana Demo", 28, "Hermana", 987654321);
        addMiembroIfMissing(nuevos, existentes, familia.getId(), "Luis Demo", 62, "Padre", 987654322);
        addMiembroIfMissing(nuevos, existentes, familia.getId(), "Maria Demo", 58, "Madre", 987654323);

        if (!nuevos.isEmpty()) {
            miembroRepository.saveAll(nuevos);
        }
    }

    private void addConsejoIfMissing(List<ConsejoPreventivo> nuevos, List<ConsejoPreventivo> existentes,
                                     String contenido, String contenidoEn, String tipoDesastre, String categoria, Integer orden) {
        boolean existe = existentes.stream().anyMatch(c -> contenido.equalsIgnoreCase(c.getContenido()));
        if (!existe) {
            nuevos.add(ConsejoPreventivo.builder()
                    .contenido(contenido)
                    .contenidoEn(contenidoEn)
                    .tipoDesastre(tipoDesastre)
                    .categoria(categoria)
                    .ordenVisualizacion(orden)
                    .build());
        }
    }

    private void addFaqIfMissing(List<FaqPregunta> nuevas, List<FaqPregunta> existentes, String pregunta,
                                 String respuesta, String categoria, Integer orden, String fuente) {
        boolean existe = existentes.stream().anyMatch(f -> pregunta.equalsIgnoreCase(f.getPregunta()));
        if (!existe) {
            nuevas.add(FaqPregunta.builder()
                    .pregunta(pregunta)
                    .respuesta(respuesta)
                    .categoria(categoria)
                    .ordenRelevancia(orden)
                    .fuenteOficial(fuente)
                    .build());
        }
    }

    private void addPuntoSeguroIfMissing(List<PuntoSeguro> nuevos, List<PuntoSeguro> existentes, String nombre,
                                         String tipo, Double latitud, Double longitud, String direccion,
                                         Integer capacidad, Boolean esOficial) {
        boolean existe = existentes.stream().anyMatch(p -> nombre.equalsIgnoreCase(p.getNombre()));
        if (!existe) {
            PuntoSeguro punto = new PuntoSeguro();
            punto.setNombre(nombre);
            punto.setTipo(tipo);
            punto.setLatitud(latitud);
            punto.setLongitud(longitud);
            punto.setDireccion(direccion);
            punto.setCapacidad(capacidad);
            punto.setEsOficial(esOficial);
            punto.setUsuario(null);
            nuevos.add(punto);
        }
    }

    private void addRefugioIfMissing(List<Refugio> nuevos, List<Refugio> existentes, String nombre, String direccion,
                                     Double latitud, Double longitud, Integer capacidadMaxima,
                                     Integer ocupacionActual, Boolean disponible, String contactoEncargado) {
        boolean existe = existentes.stream().anyMatch(r -> nombre.equalsIgnoreCase(r.getNombre()));
        if (!existe) {
            Refugio refugio = new Refugio();
            refugio.setNombre(nombre);
            refugio.setDireccion(direccion);
            refugio.setLatitud(latitud);
            refugio.setLongitud(longitud);
            refugio.setCapacidadMaxima(capacidadMaxima);
            refugio.setOcupacionActual(ocupacionActual);
            refugio.setDisponible(disponible);
            refugio.setContactoEncargado(contactoEncargado);
            nuevos.add(refugio);
        }
    }

    private void addItemKitIfMissing(List<ItemKit> nuevos, List<ItemKit> existentes, KitEmergencia kit,
                                     String nombreItem, String nombreItemEn, String categoria, Integer cantidad, Boolean tieneItem) {
        boolean existe = existentes.stream().anyMatch(i -> nombreItem.equalsIgnoreCase(i.getNombreItem()));
        if (!existe) {
            ItemKit item = new ItemKit();
            item.setKit(kit);
            item.setNombreItem(nombreItem);
            item.setNombreItemEn(nombreItemEn);
            item.setCategoria(categoria);
            item.setCantidadRecomendada(cantidad);
            item.setTieneItem(tieneItem);
            nuevos.add(item);
        }
    }

    private void addMiembroIfMissing(List<Miembros_de_familia> nuevos, List<Miembros_de_familia> existentes,
                                     Long idFamilia, String nombre, int edad, String parentezco, int contacto) {
        boolean existe = existentes.stream().anyMatch(m -> nombre.equalsIgnoreCase(m.getNombre()));
        if (!existe) {
            Miembros_de_familia miembro = new Miembros_de_familia();
            miembro.setId_familia(idFamilia);
            miembro.setNombre(nombre);
            miembro.setEdad(edad);
            miembro.setParentezco(parentezco);
            miembro.setContacto(contacto);
            nuevos.add(miembro);
        }
    }

    private void addNumeroEmergenciaIfMissing(List<NumeroEmergencia> nuevos, List<NumeroEmergencia> existentes,
                                              String nombre, String numero, String descripcion, String icono,
                                              Integer orden) {
        boolean existe = existentes.stream().anyMatch(n -> numero.equals(n.getNumero()));
        if (!existe) {
            nuevos.add(NumeroEmergencia.builder()
                    .nombre(nombre)
                    .numero(numero)
                    .descripcion(descripcion)
                    .icono(icono)
                    .ordenVisualizacion(orden)
                    .build());
        }
    }
}
