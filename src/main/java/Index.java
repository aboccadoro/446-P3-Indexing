import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;

public class Index {

    public static void main(String[] args) throws IOException, ParseException {
        JSONObject json = Index.getJSON(new JSONParser());
        //indexing begins see getIndex()
        Map<String, ArrayList<Tuple<String, ArrayList<Integer>, Character, Character>>> indices = Index.getIndex(json);
        PrintWriter writer = new PrintWriter("index.txt");
        for (String token : indices.keySet()) {
            ArrayList<Tuple<String, ArrayList<Integer>, Character, Character>> test = indices.get(token);
            writer.print(indices.get(token).size() + " " + token + "\t\t");
            for (Tuple item : test) {
                writer.print(item.w + " " + item.x.toString());
            }
            writer.println();
        }
        writer.close();
    }

    private Index() {}

    private static JSONObject getJSON(JSONParser parser) throws IOException, ParseException {
         return (JSONObject) parser.parse(new FileReader("shakespeare-scenes.json"));
    }

    private static ArrayList<Tuple> getScenes(JSONObject json) {
        JSONArray corpus = (JSONArray) json.get("corpus");
        Tuple<String, String, Long, String> scene;
        ArrayList<Tuple> scenes = new ArrayList<Tuple>();
        for (Object entry: corpus) {
            JSONObject jsonObject = (JSONObject) entry;
            String playId = (String) jsonObject.get("playId");
            String sceneId = (String) jsonObject.get("sceneId");
            Long sceneNum = (Long) jsonObject.get("sceneNum");
            String text = (String) jsonObject.get("text");
            scene = new Tuple<String, String, Long, String>(playId, sceneId, sceneNum, text);
            scenes.add(scene);
        }
        return scenes;
    }

    private static Map<String, ArrayList<Tuple<String, ArrayList<Integer>, Character, Character>>> getIndex(JSONObject json) {
        ArrayList<Tuple> scenes = Index.getScenes(json);
        Map<String, ArrayList<Tuple<String, ArrayList<Integer>, Character, Character>>> indices =
                new HashMap<String, ArrayList<Tuple<String, ArrayList<Integer>, Character, Character>>>();
        for (Tuple scene : scenes) {
            String text = (String) scene.z;
            ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(text.split("\\s+")));
            for (String token : tokens) {
                if (indices.containsKey(token)) {
                    boolean added = false;
                    for (Tuple<String, ArrayList<Integer>, Character, Character> tuple : indices.get(token)) {
                        if (tuple.w.equals(scene.x)) {
                            added = true;
                            int pos = tokens.indexOf(token);
                            int j = indices.get(token).indexOf(tuple);
                            indices.get(token).get(j).x.add(pos);
                        }
                    }
                    if (!added) {
                        ArrayList<Integer> list = new ArrayList<Integer>();
                        list.add(tokens.indexOf(token));
                        indices.get(token).add(new Tuple<String, ArrayList<Integer>, Character, Character>((String) scene.x, list, null, null));
                    }
                } else {
                    ArrayList<Integer> list = new ArrayList<Integer>();
                    list.add(tokens.indexOf(token));
                    ArrayList<Tuple<String, ArrayList<Integer>, Character, Character>> array = new ArrayList<Tuple<String, ArrayList<Integer>, Character, Character>>();
                    array.add(new Tuple<String, ArrayList<Integer>, Character, Character>((String) scene.x, list, null, null));
                    indices.put(token, array);
                }
            }
        }
        return indices;
    }
}