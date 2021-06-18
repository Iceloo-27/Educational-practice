import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {

    private static final String API_KEY = "AQVN16FQOTJL2aBQys4D94NnNi1YBtNg5chcOy6L";

    public static void main(String[] args) throws Exception {
        JSONObject jo = new JSONObject();
        JSONArray texts = new JSONArray();

        try {
            String inputText = "";
            String[] CODES = {"ar", "be", "fr", "ru", "en"};
            System.out.println("Демонстрация работы с API Yandex Translate.");
            System.out.println("Выберите язык исходного текста:");
            printListOfLanguage();
            BufferedReader is = new BufferedReader(new InputStreamReader(System.in));
            int fromNumber = Integer.parseInt(is.readLine());

            System.out.println("Введите текст для перевода: ");
            inputText = is.readLine();
            texts.put(inputText);

            System.out.println("Выберите язык на который нужно перевести текст:");
            printListOfLanguage();
            int toNumber = Integer.parseInt(is.readLine());

            System.out.println("Минутку, идет перевод...");

            String langFrom = CODES[fromNumber-1];
            String langTo = CODES[toNumber-1];

            jo.put("sourceLanguageCode", langFrom);
            jo.put("targetLanguageCode", langTo);
            jo.put("languageCode", langFrom);
            jo.put("texts", texts);
            String joString = jo.toString();

            URL url = new URL("https://translate.api.cloud.yandex.net/translate/v2/translate");

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Content-Type", "application/json");

            urlConnection.setRequestProperty("Authorization", "Api-Key " + API_KEY);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            urlConnection.setRequestProperty("file", jo.toString());
            urlConnection.connect();

            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            dataOutputStream.write(joString.getBytes());
            dataOutputStream.flush();
            dataOutputStream.close();

            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder stringBuffer = new StringBuilder();

            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuffer.append(inputLine);
            }
            bufferedReader.close();
            JSONObject JOb = new JSONObject(stringBuffer.toString());
            String resultT = JOb.getJSONArray("translations").
                    getJSONObject(0).getString("text");
            System.out.println("Результат перевода: " + resultT);
        } catch (IOException e) {
            System.out.println("IOException: " + e);
        }

    }

    private static void printListOfLanguage() {
        System.out.println("1) ar — Арабский\n" +
                "2) be — Белорусский\n" +
                "3) fr — Французский\n" +
                "4) ru — Русский\n" +
                "5) en — Английский");
        System.out.print("Введите номер (1-5): ");
    }
}