import java.io.File;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static List<Employee> employeeList = new ArrayList<>();

    public static void main(String[] args) {

        String fileNameXML = "data.xml";
        parseXML(fileNameXML);
        employeeList.forEach(System.out :: println);

        String json = listToJson(employeeList);
        String fileNameJSON = "data.json";

        writeString(json,fileNameJSON);
    }

    public static void parseXML(String nameFile) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(new File(nameFile));
            Node node = document.getDocumentElement();
            read(node);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public static void read(Node node) {

        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node1 = nodeList.item(i);
            if (Node.ELEMENT_NODE == node1.getNodeType()) {
                System.out.println("Текущий узел: " + node1.getNodeName());
                if (node1.getNodeName().equals("employee")) {
                    Employee employee = createEmployee(node1);
                    employeeList.add(employee);
                }
                Element element = (Element) node1;
                NamedNodeMap map = element.getAttributes();
                for (int j = 0; j < map.getLength(); j++) {
                    String attrName = map.item(j).getNodeName();
                    String attrValue = map.item(j).getNodeValue();
                    System.out.println("Атрибут: " + attrName + " ; Значение: " + attrValue);
                }
                read(node1);
            }
        }
    }
    private static Employee createEmployee(Node node) {
        Element element = (Element) node;
        String id = element.getElementsByTagName("id").item(0).getTextContent();
        String firstName = element.getElementsByTagName("firstName").item(0).getTextContent();
        String lastName = element.getElementsByTagName("lastName").item(0).getTextContent();
        String country = element.getElementsByTagName("country").item(0).getTextContent();
        String age = element.getElementsByTagName("age").item(0).getTextContent();
        return new Employee(Long.parseLong(id), firstName,lastName,country,Integer.parseInt(age));
    }

    public static String listToJson (List<Employee> list){

        Type listType = new TypeToken<List<Employee>>(){} .getType();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.toJson(list,listType);
    }

    public static void writeString(String jsonText , String fileName){

        try (FileWriter fileWriter = new FileWriter(fileName)){
            fileWriter.write(jsonText);
            fileWriter.flush();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}