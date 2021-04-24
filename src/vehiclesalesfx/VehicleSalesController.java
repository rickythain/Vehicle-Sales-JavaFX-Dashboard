/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehiclesalesfx;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Ricky Thain
 */
public class VehicleSalesController implements Initializable {
    
    
    private static String markup;
    private static List<Sales> sales;
    private static List<Integer> years;
    private static List<String> vehicles;
    private static List<Integer> quarters;
    private static List<String> regions;
            
    private DashService dashService;
    private CheckBox[] checkBoxes;
    private CheckBox[] checkBoxesVehicle;
    private ComboBox comboBoxesRegion;
    private ComboBox comboBoxesRegionsPC;
    private ComboBox comboBoxesYearsBC;
    private ComboBox comboBoxesRegionsBC;
    
    private ProgressIndicator ProgressIndicator1;
    
    private ComboBox comboBoxesYears;
    private PieChart pieChart1 = new PieChart();
    private PieChart pieChart2 = new PieChart();
    
    @FXML
    private AnchorPane AnchorPane1;
    
    @FXML
    private HBox HBoxVehicleChkBox;
    
    @FXML
    private HBox HBoxCombo;
    
    @FXML
    private HBox HBoxPieChart1;
    
    @FXML
    private HBox HBoxPieChart2;
    
    @FXML
    private HBox HBoxComboYear;
    
    @FXML
    private VBox VBoxLoading;
    
    @FXML
    private BarChart<?, ?> BarChart2;
    
    @FXML
    private LineChart<?, ?> LineChart3;
    
    @FXML
    private TableView TableView1;
    
    @FXML
    private TableColumn TableColumn1, TableColumn2, TableColumn3, TableColumn4, TableColumn5;
    
    @FXML
    private Label LabelGrandTotal;
    
    @FXML
    private Label LabelCurrentSales;
    
    @FXML
    private Label LabelCurrentYear;
    
    @FXML
    private Label LabelGrowthYears;
    
    @FXML
    private Label LabelGrowthRate;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        dashService = new DashService();
        dashService.setAddress("http://glynserver2.cms.livjm.ac.uk/DashService/SGetSales");
        dashService.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

            @Override
            public void handle(WorkerStateEvent e) {
                markup = e.getSource().getValue().toString();
                sales = (new Gson()).fromJson(markup, new TypeToken<LinkedList<Sales>>(){}.getType());
                /*
                System.out.println(markup);
                
                for(Sales sale:sales){
                    System.out.println(sale.toString());
                }
                */
                
                years = sales.stream().map(o -> o.getYear()).distinct().collect(Collectors.toList());
                vehicles = sales.stream().map(o -> o.getVehicle()).distinct().collect(Collectors.toList());
                quarters = sales.stream().map(o -> o.getQTR()).distinct().collect(Collectors.toList());
                regions = sales.stream().map(o -> o.getRegion()).distinct().collect(Collectors.toList());
                
                constructComboBoxes();
                constructCheckBoxes();
                getAggregateValues();
                //constructBCYearRegionSeries();
                HBoxPieChart1.getChildren().add(pieChart1);
                HBoxPieChart2.getChildren().add(pieChart2);
                
                constructSeries();
                
            }
            
        });
        
        TableColumn1.setCellValueFactory(new PropertyValueFactory<Sales, Integer>("Year"));
        TableColumn2.setCellValueFactory(new PropertyValueFactory<Sales, Integer>("QTR"));
        TableColumn3.setCellValueFactory(new PropertyValueFactory<Sales, String>("Region"));
        TableColumn4.setCellValueFactory(new PropertyValueFactory<Sales, String>("Vehicle"));
        TableColumn5.setCellValueFactory(new PropertyValueFactory<Sales, Integer>("Quantity"));
        
        ProgressIndicator1 = new ProgressIndicator();
        //ProgressIndicator1.visibleProperty().bind(dashService.runningProperty());
        VBoxLoading.visibleProperty().bind(dashService.runningProperty());
        VBoxLoading.setAlignment(Pos.CENTER);
        VBoxLoading.getChildren().add(ProgressIndicator1);
        
        dashService.start();
    }
    
    private void constructCheckBoxes() {
        
        checkBoxesVehicle = new CheckBox[vehicles.size()];
        for(byte index = 0; index < vehicles.size(); index++){
            checkBoxesVehicle[index] = new CheckBox(String.valueOf(vehicles.get(index)));
            checkBoxesVehicle[index].setSelected(true);
            checkBoxesVehicle[index].addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    // System.out.println("Firstly, Event Filters !");
                }
            });
            checkBoxesVehicle[index].addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    // System.out.println("Secondly, Event Handlers !");
                }
            });
            checkBoxesVehicle[index].setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {                    
                    // System.out.println("Thirdly, Convenience Methods !");
                    constructSeries();
                }
            });
            HBoxVehicleChkBox.getChildren().add(checkBoxesVehicle[index]);
        }
        
        AnchorPane1.getScene().getWindow().sizeToScene();
        //constructSeries();
    }
    
    private void constructComboBoxes() {
            ArrayList<String> regionsAndAll = new ArrayList<String>();
            regionsAndAll.add("All");
            regionsAndAll.addAll(regions);
            comboBoxesRegion = new ComboBox(FXCollections.observableList(regionsAndAll));
            
            comboBoxesRegion.getSelectionModel().selectFirst();
            
            comboBoxesRegion.setOnAction(new EventHandler<ActionEvent>(){

                @Override
                public void handle(ActionEvent e) {
                    constructSeries();
                }
                
            });
            HBoxCombo.getChildren().add(comboBoxesRegion);
            
            
            comboBoxesYearsBC = new ComboBox(FXCollections.observableList(years));
            comboBoxesYearsBC.getSelectionModel().selectFirst();
            comboBoxesYearsBC.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    constructBCYearRegionSeries();
                }
        
            });
            
            ArrayList<String> yearsAndAll = new ArrayList<String>();
            yearsAndAll.add("All years");
            years.forEach(year -> {yearsAndAll.add(year.toString());});
            comboBoxesYears = new ComboBox(FXCollections.observableList(yearsAndAll));
            comboBoxesYears.getSelectionModel().selectFirst();
            System.out.println("selected year: " + comboBoxesYears.getSelectionModel().getSelectedItem().toString());
            comboBoxesYears.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent event) {
                    constructSeries();
                }
        
            });
            HBoxComboYear.getChildren().add(comboBoxesYears);
            
            AnchorPane1.getScene().getWindow().sizeToScene();
            //constructSeries();
    }
    
    private void constructPieCharts() {
        //constructPCYearsSeries();
        constructPCRegionsSeries();
        System.out.println("pie: " + pieChart1.getData().toString());
        
        
    }
    
    
    private void getAggregateValues() {
        
        Integer currentYear = Collections.max(years, null);
        Integer lastYear = currentYear - 1;
        Integer grandTotal = 0;
        
        List<Sales> allSales = sales.stream().collect(Collectors.toList());
        for(Sales sale:allSales){
            grandTotal += sale.getQuantity();
        }
        
        System.out.println("grand total: " + grandTotal);
        
        LabelGrandTotal.setText(grandTotal.toString());
        
        LabelCurrentYear.setText("Total sales in " + currentYear);
        Integer totalCurrentSales = 0;
        List<Sales> sales1 = sales.stream().filter(o -> o.getYear().equals(currentYear)).collect(Collectors.toList());
        for(Sales sale:sales1){
            totalCurrentSales += sale.getQuantity();
        }
        LabelCurrentSales.setText(totalCurrentSales.toString());
        
        LabelGrowthYears.setText("Rate of growth from " + lastYear + " to " + currentYear);
        Integer totalLastYearSales = 0;
        List<Sales> sales2 = sales.stream().filter(o -> o.getYear().equals(lastYear)).collect(Collectors.toList());
        for(Sales sale:sales2){
            totalLastYearSales += sale.getQuantity();
        }
        Float growthRate = (totalCurrentSales - totalLastYearSales)/(float)totalLastYearSales * 100;
        System.out.println("c and old: " + totalCurrentSales + " " + totalLastYearSales);
        LabelGrowthRate.setText(String.format("%.2f %s", growthRate, "%"));
        
    }
    
    private void constructSeries() {
        
        TableView1.getItems().clear();
        
        // for line chart 3
        LineChart3.getData().clear();
        Integer vehicleQuantity = 0;
        Integer vehicleQuantityByVehicle = 0;
        Integer vehicleQuantityByRegion = 0;
        ObservableList<Data> salesPCVehicles = FXCollections.observableArrayList();
        ObservableList<Data> salesPCRegions = FXCollections.observableArrayList();
        
        mainloop:
        for(CheckBox chkVehicle:checkBoxesVehicle){
            if(chkVehicle.isSelected()){
                XYChart.Series lineSeries = new XYChart.Series();
                lineSeries.setName(chkVehicle.getText());
                
                
                if(comboBoxesRegion.getValue().toString().equalsIgnoreCase("all")){
                    
                    if(comboBoxesYears.getValue().toString().equalsIgnoreCase("all years")){
                        vehicleQuantityByVehicle = 0;
                        for(Integer year:years){
                            for(Integer quarter:quarters){
                                vehicleQuantity = 0;
                                List<Sales> saleList = sales.stream()
                                        .filter(o -> o.getVehicle().equals(chkVehicle.getText()))
                                        .filter(o -> o.getYear().equals(year))
                                        .filter(o -> o.getQTR().equals(quarter))
                                        .collect(Collectors.toList());
                                
                                for(Sales sale:saleList){
                                    vehicleQuantity += sale.getQuantity();
                                }
                                vehicleQuantityByVehicle += vehicleQuantity;
                                lineSeries.getData().add(new XYChart.Data<>(year.toString() + "-Q" + quarter.toString(), vehicleQuantity));
                                TableView1.getItems().add(new Sales(quarter, vehicleQuantity, comboBoxesRegion.getValue().toString(), chkVehicle.getText(), year));
                            }
                        }
                        
                    } else {
                        vehicleQuantityByVehicle = 0;
                        for(Integer quarter:quarters){
                            vehicleQuantity = 0;
                            List<Sales> saleList = sales.stream()
                                    .filter(o -> o.getVehicle().equals(chkVehicle.getText()))
                                    .filter(o -> o.getYear().equals(Integer.valueOf(comboBoxesYears.getValue().toString())))
                                    .filter(o -> o.getQTR().equals(quarter))
                                    .collect(Collectors.toList());
                            
                            for(Sales sale:saleList){
                                vehicleQuantity += sale.getQuantity();
                            }
                            vehicleQuantityByVehicle += vehicleQuantity;
                            lineSeries.getData().add(new XYChart.Data<>("Q" + quarter.toString(), vehicleQuantity));
                            TableView1.getItems().add(new Sales(quarter, vehicleQuantity, comboBoxesRegion.getValue().toString(), chkVehicle.getText(), Integer.valueOf(comboBoxesYears.getValue().toString())));
                        }
                    }
                    
                } else {
                    // else specfic region is selected
                    if(comboBoxesYears.getValue().toString().equalsIgnoreCase("all years")){
                        vehicleQuantityByVehicle = 0;
                        for(Integer year:years){
                            for(Integer quarter:quarters){
                                vehicleQuantity = 0;
                                List<Sales> saleList = sales.stream()
                                        .filter(o -> o.getVehicle().equals(chkVehicle.getText()))
                                        .filter(o -> o.getYear().equals(year))
                                        .filter(o -> o.getQTR().equals(quarter))
                                        .filter(o -> o.getRegion().equals(comboBoxesRegion.getValue().toString()))
                                        .collect(Collectors.toList());
                                
                                for(Sales sale:saleList){
                                    vehicleQuantity += sale.getQuantity();
                                }
                                vehicleQuantityByVehicle += vehicleQuantity;
                                lineSeries.getData().add(new XYChart.Data<>(year.toString() + "-Q" + quarter.toString(), vehicleQuantity));
                                TableView1.getItems().add(new Sales(quarter, vehicleQuantity, comboBoxesRegion.getValue().toString(), chkVehicle.getText(), year));
                            }
                        }
                    } else {
                        vehicleQuantityByVehicle = 0;
                        for(Integer quarter:quarters){
                            vehicleQuantity = 0;
                            List<Sales> saleList = sales.stream()
                                    .filter(o -> o.getVehicle().equals(chkVehicle.getText()))
                                    .filter(o -> o.getYear().equals(Integer.valueOf(comboBoxesYears.getValue().toString())))
                                    .filter(o -> o.getQTR().equals(quarter))
                                    .filter(o -> o.getRegion().equals(comboBoxesRegion.getValue().toString()))
                                    .collect(Collectors.toList());
                            
                            for(Sales sale:saleList){
                                vehicleQuantity += sale.getQuantity();
                            }
                            vehicleQuantityByVehicle += vehicleQuantity;
                            lineSeries.getData().add(new XYChart.Data<>("Q" + quarter.toString(), vehicleQuantity));
                            TableView1.getItems().add(new Sales(quarter, vehicleQuantity, comboBoxesRegion.getValue().toString(), chkVehicle.getText(), Integer.valueOf(comboBoxesYears.getValue().toString())));
                        }
                    }
                }
                LineChart3.getData().add(lineSeries);
                salesPCVehicles.add(new PieChart.Data(chkVehicle.getText(), vehicleQuantityByVehicle));
            }
        }
        pieChart1.setData(salesPCVehicles);
        pieChart1.setTitle("Sales by Vehicle");
        
        // for pie chart of sales by regionx
        if(comboBoxesRegion.getValue().toString().equalsIgnoreCase("all")){
            for(String region:regions){
                vehicleQuantityByRegion = 0;
                if(comboBoxesYears.getValue().toString().equalsIgnoreCase("all years")){
                    for(CheckBox chkVehicle:checkBoxesVehicle){
                        if(chkVehicle.isSelected()){
                            List<Sales> saleList = sales.stream()
                                    .filter(o -> o.getRegion().equals(region))
                                    .filter(o -> o.getVehicle().equals(chkVehicle.getText()))
                                    .collect(Collectors.toList());
                            for(Sales sale:saleList){
                                vehicleQuantityByRegion += sale.getQuantity();
                            }
                        }
                    }
                } else {
                    for(CheckBox chkVehicle:checkBoxesVehicle){
                        if(chkVehicle.isSelected()){
                            List<Sales> saleList = sales.stream()
                                    .filter(o -> o.getRegion().equals(region))
                                    .filter(o -> o.getVehicle().equals(chkVehicle.getText()))
                                    .filter(o -> o.getYear().equals(Integer.valueOf(comboBoxesYears.getValue().toString())))
                                    .collect(Collectors.toList());
                            for(Sales sale:saleList){
                                vehicleQuantityByRegion += sale.getQuantity();
                            }
                        }
                    }
                }
                salesPCRegions.add(new PieChart.Data(region, vehicleQuantityByRegion));
            }
        } else {
            vehicleQuantityByRegion = 0;
                if(comboBoxesYears.getValue().toString().equalsIgnoreCase("all years")){
                    for(CheckBox chkVehicle:checkBoxesVehicle){
                        if(chkVehicle.isSelected()){
                            List<Sales> saleList = sales.stream()
                                    .filter(o -> o.getRegion().equals(comboBoxesRegion.getValue().toString()))
                                    .filter(o -> o.getVehicle().equals(chkVehicle.getText()))
                                    .collect(Collectors.toList());
                            for(Sales sale:saleList){
                                vehicleQuantityByRegion += sale.getQuantity();
                            }
                        }
                    }
                } else {
                    for(CheckBox chkVehicle:checkBoxesVehicle){
                        if(chkVehicle.isSelected()){
                            List<Sales> saleList = sales.stream()
                                    .filter(o -> o.getRegion().equals(comboBoxesRegion.getValue().toString()))
                                    .filter(o -> o.getVehicle().equals(chkVehicle.getText()))
                                    .filter(o -> o.getYear().equals(Integer.valueOf(comboBoxesYears.getValue().toString())))
                                    .collect(Collectors.toList());
                            for(Sales sale:saleList){
                                vehicleQuantityByRegion += sale.getQuantity();
                            }
                        }
                    }
                }
                salesPCRegions.add(new PieChart.Data(comboBoxesRegion.getValue().toString(), vehicleQuantityByRegion));
        }
        
        pieChart2.setData(salesPCRegions);
        pieChart2.setTitle("Sales by Region");
        
        // for bar chart sales by kuarters
        BarChart2.getData().clear();
        Integer vehicleQuantityByQuarter = 0;
        XYChart.Series barSeries = new XYChart.Series();
        
        for(Integer quarter:quarters){
            vehicleQuantityByQuarter = 0;
            for(CheckBox chkVehicle:checkBoxesVehicle){
                if(chkVehicle.isSelected()){
                    if(comboBoxesRegion.getValue().toString().equalsIgnoreCase("all") && comboBoxesYears.getValue().toString().equalsIgnoreCase("all years")){
                        List<Sales> saleList = sales.stream()
                                .filter(o -> o.getQTR().equals(quarter))
                                .collect(Collectors.toList());
                        for(Sales sale:saleList){
                            vehicleQuantityByQuarter += sale.getQuantity();
                        }
                    } else if(comboBoxesRegion.getValue().toString().equalsIgnoreCase("all") && !(comboBoxesYears.getValue().toString().equalsIgnoreCase("all years"))){
                        List<Sales> saleList = sales.stream()
                                .filter(o -> o.getQTR().equals(quarter))
                                .filter(o -> o.getYear().equals(Integer.valueOf(comboBoxesYears.getValue().toString())))
                                .collect(Collectors.toList());
                        for(Sales sale:saleList){
                            vehicleQuantityByQuarter += sale.getQuantity();
                        }
                    } else if(!(comboBoxesRegion.getValue().toString().equalsIgnoreCase("all")) && comboBoxesYears.getValue().toString().equalsIgnoreCase("all years")){
                        List<Sales> saleList = sales.stream()
                                .filter(o -> o.getQTR().equals(quarter))
                                .filter(o -> o.getRegion().equals(comboBoxesRegion.getValue().toString()))
                                .collect(Collectors.toList());
                        for(Sales sale:saleList){
                            vehicleQuantityByQuarter += sale.getQuantity();
                        }
                    } else {
                        List<Sales> saleList = sales.stream()
                                .filter(o -> o.getQTR().equals(quarter))
                                .filter(o -> o.getRegion().equals(comboBoxesRegion.getValue().toString()))
                                .filter(o -> o.getYear().equals(Integer.valueOf(comboBoxesYears.getValue().toString())))
                                .collect(Collectors.toList());
                        for(Sales sale:saleList){
                            vehicleQuantityByQuarter += sale.getQuantity();
                        }
                    }
                }
            }
            barSeries.getData().add(new XYChart.Data<>(quarter.toString(), vehicleQuantityByQuarter));
        }
        BarChart2.getData().add(barSeries);
        BarChart2.setTitle("Vehicle sales by quarter");
    }
    
    private void constructPCRegionsSeries() {
        // insert data into pie chart vehicles in a region
        for (String region:regions){
            Integer totalSalesPerVehicle =  0;
            if (comboBoxesRegionsPC.getSelectionModel().getSelectedItem().equals(region)){
                ObservableList<Data> salesList = FXCollections.observableArrayList();
                for (String vehicle:vehicles){
                    List<Sales> sales5 = sales.stream().filter(o -> o.getRegion().equals(region)).filter(o -> o.getVehicle().equals(vehicle)).collect(Collectors.toList());
                    for(Sales sale:sales5){
                        totalSalesPerVehicle += sale.getQuantity();
                    }
                    salesList.add(new PieChart.Data(vehicle, totalSalesPerVehicle));
                }
                System.out.println("sales list: " + salesList);
                pieChart2.setData(salesList);
        pieChart2.setTitle("Sales of ");
            }
        }
    }
    
    private void constructBCYearRegionSeries() {
        BarChart2.getData().clear();
        Integer totalSales = 0;
        XYChart.Series series = new XYChart.Series();
        for(String vehicle:vehicles){
            
            totalSales = 0;
            List<Sales> sales5 = sales.stream().filter(o -> o.getYear().equals(Integer.valueOf(comboBoxesYearsBC.getSelectionModel().getSelectedItem().toString()))).filter(o -> o.getRegion().equals(comboBoxesRegionsBC.getSelectionModel().getSelectedItem().toString())).filter(o -> o.getVehicle().equals(vehicle)).collect(Collectors.toList());
            for (Sales sale:sales5){
                totalSales += sale.getQuantity();
            }
            series.getData().add(new XYChart.Data<>(vehicle, totalSales));
            
        }
        BarChart2.getData().add(series);
        BarChart2.setTitle("Sales of each vehicle in " + comboBoxesRegionsBC.getSelectionModel().getSelectedItem().toString() + " in " + comboBoxesYearsBC.getSelectionModel().getSelectedItem().toString());
    }
    
    private static class DashService extends Service<String>{
        private StringProperty address = new SimpleStringProperty();

        public final void setAddress(String address){
            this.address.set(address);
        }
        
        public final String getAddress(){
            return address.get();
        }
        
        public final StringProperty addressProperty(){
            return address;
        }
        
        @Override
        protected Task<String> createTask() {
            return new Task<String>(){
                private HttpURLConnection httpuc;
                private String markup;

                @Override
                protected String call() {
                    try {
                        httpuc = (HttpURLConnection)(new URL(getAddress())).openConnection();
                        httpuc.setRequestMethod("GET");
                        httpuc.setRequestProperty("Accept", "application/json");
                        httpuc.setRequestProperty("Content-Type", "application/json");
                        
                        markup = (new BufferedReader(new InputStreamReader(httpuc.getInputStream()))).readLine();
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if(httpuc != null) {
                            httpuc.disconnect();
                        }
                    }
                    
                    return markup;
                }
            };
        }
    }
    
    @FXML
    public void radioBtnTimeOnAction(Event e) {
        System.out.println("radio btn on action");
        constructSeries();
    }
    
    @FXML
    public void menuExitOnAction(ActionEvent e) {
        Platform.exit();
    }
    
    
    
}
