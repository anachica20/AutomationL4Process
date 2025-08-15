import java.util.*;
import java.util.stream.Collectors;

public class Settings {

    public List<Section> sections;
    public String baseUrl;

    public Settings() {
        this.sections = new ArrayList<>();
        this.baseUrl ="https://d2hppmf8y0hyf4.cloudfront.net/"; //DEV: "https://lib4dev.liberascf.com/"; //QA: https://d2hppmf8y0hyf4.cloudfront.net/sign-in //PROD: "https://scfpdn.liberascf.com/"

        List<DeclaracionItem> declaracionList = Arrays.asList(
                new DeclaracionItem("datos_personales", "Protección de datos personales", 1,1),//mat-checkbox-3-input
                new DeclaracionItem("habeas_data", "Hábeas Data", 1,1),
                new DeclaracionItem("origen_fondos", "Origen de fondos", 1,1),
                new DeclaracionItem("prevencion_riegos", "Prevención de riesgo de lavado de activos y financiación del terrorismo.", 1,1),
                new DeclaracionItem("transparencia_etica_empresarial", "Transparencia y Ética Empresarial.", 1,1),
                new DeclaracionItem("declaracion_informacion_proporcionada", "Declaración de información proporcionada", 1,1),
                new DeclaracionItem("linea_etica", "Línea Ética", 1,1),
                new DeclaracionItem("listas_restrictivas", "Autorizo que la información proporcionada en este formulario se utilice para buscar registros del proveedor en listas restrictivas", 1,0),
                new DeclaracionItem("seguridad_informacion", "Estoy de acuerdo con la política general de seguridad de la información", 1,1),
                new DeclaracionItem("tratamiento_datos", "Estoy de acuerdo con la política de tratamiento de datos", 1,1),
                new DeclaracionItem("terminos_condiciones", "Estoy de acuerdo con los términos y condiciones", 1,1),
                new DeclaracionItem("constancia_mandato", "Constancia de mandato", 1,1)
        );

        List<DeclaracionItem> documentsList = Arrays.asList(
                new DeclaracionItem("arl", "C://Users//ana.chica//Documents//empty.pdf", 1),//mat-checkbox-3-input
                new DeclaracionItem("certificacion", "C://Users//ana.chica//Documents//empty.pdf", 1),
                new DeclaracionItem("cedula_rep_legal", "C://Users//ana.chica//Documents//empty.pdf", 1),
                new DeclaracionItem("camara_comercio", "C://Users//ana.chica//Documents//empty.pdf", 1),
                new DeclaracionItem("rut", "C://Users//ana.chica//Documents//empty.pdf", 1)
        );

        Section declaracionSection = new Section("declaracion", declaracionList, "Declaraciones, autorizaciones y garantías");
        this.sections.add(declaracionSection);

        Section documentsSections = new Section("documentos", documentsList, "Documentos");
        this.sections.add(documentsSections);

        Section mandatoSection = new Section("constacia de mandato", declaracionList, "Declaraciones, autorizaciones y garantías");
        this.sections.add(mandatoSection);
    }

    public static class Section {
        public String nombre;
        public List<DeclaracionItem> items;
        public String selector;

        public Section(String nombre, List<DeclaracionItem> items, String selector) {
            this.nombre = nombre;
            this.items = items;
            this.selector = selector;
        }
    }

    public static class DeclaracionItem {
        public String nombre;
        public String value;
        public int active;
        public Integer confirm;

        public DeclaracionItem(String nombre, String value, int active, Integer confirm) {
            this.nombre = nombre;
            this.value = value;
            this.active = active;
            this.confirm = confirm;
        }
        public DeclaracionItem(String nombre, String value, int active) {
            this(nombre, value, active, null); // confirm es opcional
        }
    }
}
