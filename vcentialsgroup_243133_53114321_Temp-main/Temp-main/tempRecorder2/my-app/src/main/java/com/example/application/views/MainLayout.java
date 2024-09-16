package com.example.application.views;

import com.example.application.security.Auth;
import com.example.application.security.AuthenticatedUser;
import com.example.application.views.about.AboutView;

import com.example.application.views.admin.AdminView;
import com.example.application.views.home.HomeView;
//import com.example.application.views.Metrics.MetricsView;
import com.example.application.views.profile.ProfileView;
import com.google.firebase.auth.FirebaseToken;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.lineawesome.LineAwesomeIcon;

import java.util.Optional;

/**
 * The main view is a top-level placeholder for other views.
 */

public class MainLayout extends AppLayout implements BeforeEnterObserver {

    private H1 viewTitle;

    private final AuthenticatedUser authenticatedUser;


    public MainLayout(@Autowired AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private HorizontalLayout addLogoutButton(){
        HorizontalLayout header;
        Button logout = new Button("Logout", click -> authenticatedUser.logout());
        logout.setPrefixComponent(VaadinIcon.SIGN_OUT.create());
        logout.addClassName("button2");
        header = new HorizontalLayout(logout);
        header.getStyle().set("margin-left","auto");
        return header;
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");
        toggle.addClassName("button2");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
        addToNavbar(true, addLogoutButton());

    }

    private void addDrawerContent() {
        Span appName = new Span("VCentials Temperature Recorder");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation() {
        SideNav nav = new SideNav();

        nav.addItem(new SideNavItem("Home", HomeView.class, LineAwesomeIcon.COLUMNS_SOLID.create()));
        //nav.addItem(new SideNavItem("Metrics", MetricsView.class, LineAwesomeIcon.CHART_BAR.create()));
        //nav.addItem(new SideNavItem("Settings", SettingsView.class, LineAwesomeIcon.COG_SOLID.create()));
        nav.addItem(new SideNavItem("Profile", ProfileView.class, LineAwesomeIcon.USER.create())); // Add ProfileView.java
        addAdmin(nav);
        nav.addItem(new SideNavItem("About", AboutView.class, LineAwesomeIcon.COMMENT_SOLID.create()));

        return nav;
    }


    // Add admin page only if admin user is logged in
    private void addAdmin(SideNav nav){
        if(Auth.isAdmin(authenticatedUser)){
            nav.addItem(new SideNavItem("Admin", AdminView.class, LineAwesomeIcon.CHESS_BOARD_SOLID.create()));
        }
    }


    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }

    //Instead of having a view be an empty string we can redirect to one of the other views when it's an empty string. defaulting to grid-with-filters page.
    //cole
    @Override
    public void beforeEnter(BeforeEnterEvent e){
        if(e.getLocation().getPath().isEmpty()){
            //e.forwardTo("");
        }
        if (authenticatedUser == null || authenticatedUser.get().isEmpty()){
            //e.forwardTo("");
        }
    }
}
