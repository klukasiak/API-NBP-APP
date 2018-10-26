package tk.kdev.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.google.gson.Gson;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import tk.kdev.model.DataObject;
import tk.kdev.model.Rate;

public class MainPaneController implements Initializable {

	@FXML
	private Label titleLabel;

	@FXML
	private ChoiceBox<String> currencyList;

	@FXML
	private DatePicker fromDate;

	@FXML
	private DatePicker toDate;

	@FXML
	private Button submitButton;
	
	@FXML
	private Label answer;
	
	@FXML
	private Label secondAnswer;

	private String fromDateString;

	private String toDateString;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		currencyList.getItems().addAll("EUR", "USD", "GBP");
		String pattern = "yyyy-MM-dd";
		submitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				toDateString = toDate.getValue().format(DateTimeFormatter.ofPattern(pattern));
				fromDateString = fromDate.getValue().format(DateTimeFormatter.ofPattern(pattern));
				System.out.println(toDateString);
				System.out.println(fromDateString);
				String url = "http://api.nbp.pl/api/exchangerates/rates/c/" + currencyList.getValue() + "/" + fromDateString + "/" + toDateString + "/?format=json";
				try {
					String json = readUrl(url);
					
					Gson gson = new Gson();
					DataObject dataObject = gson.fromJson(json, DataObject.class);
					
					float sum = 0;
					float average = 0;
					double deviation = 0;
					for (Rate rate : dataObject.getRates()) {
						sum += Float.parseFloat(rate.getBid());
						average += Float.parseFloat(rate.getAsk());
					}
					average /= dataObject.getRates().size();
					System.out.println(average);
					for (Rate rate : dataObject.getRates()) {
						deviation += Math.pow(Float.parseFloat(rate.getAsk()) - average, 2);
					}
					deviation = Math.sqrt(deviation/dataObject.getRates().size());
					System.out.println(sum/dataObject.getRates().size());
					String sumText = String.valueOf(sum/dataObject.getRates().size());
					String deviationText = String.valueOf(deviation);
					answer.setText(sumText.substring(0,6));
					secondAnswer.setText(deviationText.substring(0,6));
				} catch (Exception e) {
					System.out.println(e);
				}
				
			}

		});
	}

	private static String readUrl(String urlString) throws Exception {
		BufferedReader reader = null;
		try {
			URL url = new URL(urlString);
			reader = new BufferedReader(new InputStreamReader(url.openStream()));
			StringBuffer buffer = new StringBuffer();
			int read;
			char[] chars = new char[1024];
			while ((read = reader.read(chars)) != -1)
				buffer.append(chars, 0, read);

			return buffer.toString();
		} finally {
			if (reader != null)
				reader.close();
		}
	}
}