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
import tk.kdev.model.RatesTable;
import tk.kdev.model.Rate;

public class MainPaneController implements Initializable {

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
	
	@FXML
	private Label errorMessage;

	private String fromDateString;

	private String toDateString;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		errorMessage.setText("");
		currencyList.getItems().addAll("EUR", "USD", "GBP", "AUD", "CAD", "HUF", "CHF", "JPY", "CZK", "DKK", "NOK", "SEK", "XDR");
		String pattern = "yyyy-MM-dd";
		submitButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				errorMessage.setText("");
				try {
					toDateString = toDate.getValue().format(DateTimeFormatter.ofPattern(pattern));
					fromDateString = fromDate.getValue().format(DateTimeFormatter.ofPattern(pattern));
					String url = "http://api.nbp.pl/api/exchangerates/rates/c/" + currencyList.getValue() + "/"
							+ fromDateString + "/" + toDateString + "/?format=json";
					String json = readUrl(url);
					Gson gson = new Gson();
					RatesTable ratesTable = gson.fromJson(json, RatesTable.class);

					float sum = 0;
					float average = 0;
					double deviation = 0;

					for (Rate rate : ratesTable.getRates()) {
						sum += Float.parseFloat(rate.getBid());
						average += Float.parseFloat(rate.getAsk());
					}

					average /= ratesTable.getRates().size();

					for (Rate rate : ratesTable.getRates()) {
						deviation += Math.pow(Float.parseFloat(rate.getAsk()) - average, 2);
					}

					deviation = Math.sqrt(deviation / ratesTable.getRates().size());

					String sumText = String.valueOf(sum / ratesTable.getRates().size());
					String deviationText = String.valueOf(deviation);
					answer.setText("Average Bid: " + sumText.substring(0, 6));
					secondAnswer.setText("Standard Ask Deviation: " + deviationText.substring(0, 6));
				} catch (Exception e) {
					errorMessage.setText("Error");
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