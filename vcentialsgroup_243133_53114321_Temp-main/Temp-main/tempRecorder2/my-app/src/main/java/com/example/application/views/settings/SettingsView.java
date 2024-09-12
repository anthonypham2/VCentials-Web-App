
package com.example.application.views.settings;

import com.example.application.data.Locations;
import com.example.application.services.LocationsService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.PermitAll;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;
@PageTitle("Settings")
@Route(value = "setting", layout = MainLayout.class)
@RouteAlias(value = "Setting", layout = MainLayout.class)
@PermitAll
public class SettingsView extends Composite<VerticalLayout> {

    private final LocationsService locationsService;

    public SettingsView(LocationsService locationsService)  {
        this.locationsService = locationsService;
        FormLayout formLayout = new FormLayout();
        formLayout.setMaxWidth("800px");

        Select<String> themeSelect = new Select<>();
        themeSelect.setLabel("Theme");
        themeSelect.setValue("Light");
        themeSelect.setItems("Light", "Dark", "System Default");

        Select<String> unitSelect = new Select<>();
        unitSelect.setLabel("Unit of Measurement");
        unitSelect.setValue("Fahrenheit");
        unitSelect.setItems("Fahrenheit", "Celsius");

        ComboBox<Locations> locationCB = new ComboBox<>("Location");
        locationCB.setItems(query -> locationsService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query))).stream());
        locationCB.setItemLabelGenerator(Locations::getLocationName);
        locationCB.setPlaceholder("Select a location");



        formLayout.add(locationCB, unitSelect, themeSelect);

        VerticalLayout wrapper = new VerticalLayout(formLayout);
        wrapper.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        wrapper.setWidth("50%");

        getContent().add(new H3("Settings"), wrapper);
        getContent().setSizeFull();
        getContent().setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        getContent().setAlignItems(Alignment.CENTER);
    }
}