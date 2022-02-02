package org.vaadin.example;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import database.DatabaseConfig;
import database.booking.BookingDAO;
import database.customer.CustomerDAO;
import database.movie.MovieDAO;
import database.price.PriceDAO;
import database.representation.RepresentationDAO;
import database.screen.ScreenDAO;
import database.theatre.TheatreDAO;
import database.ticket.TicketDAO;
import model.*;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Route("")
public class MainView extends VerticalLayout {

    private Customer customer;
    private int weekDay;
    private int cinema;
    private VerticalLayout movieRepresentations;

    public MainView() {
        customer = new Customer();
        weekDay = new Integer(1);
        cinema = new Integer(1);

        this.getElement().getStyle().set("background-color","#f9003a");
        VerticalLayout todosList = new VerticalLayout();
        TextField taskField = new TextField();
        Button addButton = new Button("Add");
        addButton.addClickListener(click -> {
            Checkbox checkbox = new Checkbox(taskField.getValue());
            todosList.add(checkbox);

        });
        addButton.addClickShortcut(Key.ENTER);
        this.setMargin(false);
        this.setSpacing(false);
        this.setPadding(false);

        HorizontalLayout title = new HorizontalLayout();
        Label pageTitle = new Label("FOCUS CINEMA");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #ffffff, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");

        Dialog dialogLogIn = new Dialog();

        HorizontalLayout dialogLayoutLogIn = createDialogLayoutLogIn(dialogLogIn, customer);
        dialogLogIn.add(dialogLayoutLogIn);

        Button logIn = new Button("Log in", e -> dialogLogIn.open());
        logIn.setWidth("100px");
        logIn.getElement().getStyle().set("background","transparent");
        logIn.getElement().getStyle().set("color","#ffffff");
        logIn.getElement().getStyle().set("font-size","20px");
        logIn.getElement().getStyle().set("font-family","Rockwell");
        logIn.getElement().getStyle().set("margin-top", "25px");
        logIn.getElement().getStyle().set("margin-left", "1280px");


        final StreamResource imageResource = new StreamResource("MyResourceName", () -> {
            try
            {
                return new FileInputStream(new File("E:\\my-todo1\\my-todo\\src\\main\\resources\\cog.png"));
            }
            catch(final FileNotFoundException e)
            {
                e.printStackTrace();
                return null;
            }
        });

        Image cog = new Image(imageResource, "Couldn't load image :(");
        cog.setHeight(20, Unit.PIXELS);
        cog.getElement().getStyle().set("margin-top", "33px");
        cog.addClickListener(e -> {
            Dialog dialogAccount = new Dialog();
            VerticalLayout dialogAccountV = createDialogLayoutCog(dialogAccount, this.customer);
            dialogAccount.add(dialogAccountV);
            dialogAccount.open();
        });

        title.add(pageTitle);
        title.add(dialogLogIn, logIn);
        title.add(cog);
        title.setMargin(false);
        title.getElement().getStyle().set("margin-left","15px");
        add(title);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        MovieDAO movieDAO = context.getBean(MovieDAO.class);
        List<Movie> movieList = movieDAO.getAllMovies();
        context.close();

        HorizontalLayout moviePosters = new HorizontalLayout();
        for(int i = 0; i < movieList.size(); i++){
            Dialog dialogTrailer = new Dialog();
            VerticalLayout dialogLayout = createDialogLayout(dialogTrailer, movieList.get(i));
            dialogTrailer.add(dialogLayout);

            Image image = new Image(movieList.get(i).getPoster(), "movie" + i);
            image.setHeight(420, Unit.PIXELS);
            image.addClickListener(e -> dialogTrailer.open());
            image.getElement().getStyle().set("cursor", "pointer");
            moviePosters.add(image);
        }
        VerticalLayout panel = new VerticalLayout();
        panel.getStyle().set("overflow", "auto");
        panel.getStyle().set("border", "0px solid");
        panel.setWidth(this.getWidth());
        panel.setHeight("475px");
        add(panel);
        panel.add(moviePosters);
        panel.getElement().getStyle().set("background-color","#53cad7");

        HorizontalLayout weekBar = new HorizontalLayout();

        weekBar.getElement().getStyle().set("padding-left","15px");
        weekBar.getElement().getStyle().set("padding-right","15px");
        weekBar.getElement().getStyle().set("padding-top","15px");
        weekBar.getElement().getStyle().set("padding-bottom","15px");

        List<String> days = new ArrayList<>();
        days.add("Astazi");
        days.add("Vi");
        days.add("Sa");
        days.add("Du");
        days.add("Lu");
        days.add("Ma");
        days.add("Mi");

        for(int i = 0; i < days.size(); i++){
            int finalI = i;
            Button day = new Button(days.get(i), e -> {
                weekDay = finalI + 1;
                VerticalLayout newMovieRepresentation = createMovieRepresentations(weekDay, cinema, movieList);
                this.replace(movieRepresentations, newMovieRepresentation);
                movieRepresentations = newMovieRepresentation;
            });
            if(i == 0)
                day.setWidth("120px");
            else
                day.setWidth("60px");
            day.getElement().getStyle().set("background","transparent");
            day.getElement().getStyle().set("color","#ffffff");
            day.getElement().getStyle().set("font-size","20px");
            day.getElement().getStyle().set("font-family","Rockwell");
            weekBar.add(day);
        }
        add(weekBar);

        AnnotationConfigApplicationContext theatreContext = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        TheatreDAO theatreDAO = theatreContext.getBean(TheatreDAO.class);
        List<Theatre> theatreList = theatreDAO.getAllTheatres();
        theatreContext.close();

        Select<Button> cinemas = new Select<>();
        cinemas.getElement().getStyle().set("margin-left","750px");
        cinemas.setPlaceholder("Alege cinema-ul");

        for(int i = 0; i < theatreList.size(); i++){
            int finalI = i;
            Button theatre = new Button(theatreList.get(i).getLocation(), e -> {
                cinema = finalI + 1;
                VerticalLayout newMovieRepresentation = createMovieRepresentations(weekDay, cinema, movieList);
                this.replace(movieRepresentations, newMovieRepresentation);
                movieRepresentations = newMovieRepresentation;
            });
            theatre.getElement().getStyle().set("font-size","17px");
            theatre.getElement().getStyle().set("font-family","Rockwell");
            theatre.getElement().getStyle().set("background-image","linear-gradient(45deg, #000000, #f9003a)");
            theatre.getElement().getStyle().set("-webkit-background-clip","text");
            theatre.getElement().getStyle().set("-webkit-text-fill-color","transparent");
            cinemas.add(theatre);
        }
        weekBar.add(cinemas);

        Select<Button> movies = new Select<>();
        movies.getElement().getStyle().set("margin-left","30px");
        movies.setPlaceholder("Alege filmul");

        for(int i = 0; i < movieList.size() ; i++){
            int finalI = i;
            Button movieButton = new Button(movieList.get(i).getTitle(), e -> {
                Dialog dialogProgramme = new Dialog();

                HorizontalLayout dialogProgrammeH = createDialogLayoutMovieProgramme(dialogProgramme, movieList.get(finalI));
                dialogProgramme.add(dialogProgrammeH);
                dialogProgramme.open();
            });
            movieButton.getElement().getStyle().set("font-size","17px");
            movieButton.getElement().getStyle().set("font-family","Rockwell");
            movieButton.getElement().getStyle().set("background-image","linear-gradient(45deg, #000000, #f9003a)");
            movieButton.getElement().getStyle().set("-webkit-background-clip","text");
            movieButton.getElement().getStyle().set("-webkit-text-fill-color","transparent");
            movies.add(movieButton);
        }
        weekBar.add(movies);

        movieRepresentations = createMovieRepresentations(weekDay, cinema, movieList);
        add(movieRepresentations);
    }

    private VerticalLayout createMovieRepresentations(int weekDay, int cinema, List<Movie> movieList){
        VerticalLayout movieRepresentations = new VerticalLayout();
        movieRepresentations.getElement().getStyle().set("background-color","#53cad7");

        AnnotationConfigApplicationContext contextRepresentation = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        RepresentationDAO representationDAO = contextRepresentation.getBean(RepresentationDAO.class);
        List<Representation> representationList = representationDAO.getAllRepresentationsInTheatreForDay(cinema, weekDay);
        contextRepresentation.close();

        for(int i = 0; i < movieList.size(); i++){
            Label movieName = new Label(movieList.get(i).getTitle());
            movieName.getElement().getStyle().set("font-size","30px");
            movieName.getElement().getStyle().set("font-family","Rockwell");
            movieName.getElement().getStyle().set("background-image","linear-gradient(45deg, #000000, #f9003a)");
            movieName.getElement().getStyle().set("-webkit-background-clip","text");
            movieName.getElement().getStyle().set("-webkit-text-fill-color","transparent");
            movieRepresentations.add(movieName);

            HorizontalLayout movieStartHours = new HorizontalLayout();

            for(Representation r : representationList)
                if(r.getMovie().equals(i + 1)){
                    Button representation = new Button(r.getStartTime(), e -> {
                        Dialog dialogReservation = new Dialog();

                        VerticalLayout dialogReservationV = createDialogLayoutBooking(dialogReservation, r, customer);
                        dialogReservation.add(dialogReservationV);
                        dialogReservation.open();
                    });
                    representation.getElement().getStyle().set("background","#f9003a");
                    representation.getElement().getStyle().set("color","#ffffff");
                    representation.getElement().getStyle().set("font-size","15px");
                    representation.getElement().getStyle().set("font-family","Rockwell");
                    movieStartHours.add(representation);
                }

            movieRepresentations.add(movieStartHours);
            Label movieLine = new Label("____________________________________________________");
            movieLine.getElement().getStyle().set("font-size","30px");
            movieLine.getElement().getStyle().set("font-family","Rockwell");
            movieLine.getElement().getStyle().set("background-image","linear-gradient(45deg, #000000, #f9003a)");
            movieLine.getElement().getStyle().set("-webkit-background-clip","text");
            movieLine.getElement().getStyle().set("-webkit-text-fill-color","transparent");
            movieRepresentations.add(movieLine);
        }
        return movieRepresentations;
    }

    private HorizontalLayout createDialogLayoutLogIn(Dialog dialogLogIn, Customer customer) {
        HorizontalLayout dialogLayout = new HorizontalLayout();
        dialogLayout.setPadding(false);

        VerticalLayout leftLayout = new VerticalLayout();
        Image image = new Image("https://static.ssb.ee/images/emtakpics/46441-0006.jpg", "logIn");
        image.setHeight(420, Unit.PIXELS);
        leftLayout.add(image);

        Label pageTitle = new Label("FOCUS CINEMA");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        leftLayout.add(pageTitle);

        VerticalLayout rightLayout = new VerticalLayout();

        Label connect = new Label("Conectează-te");
        connect.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        connect.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        connect.getElement().getStyle().set("-webkit-background-clip","text");
        connect.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        rightLayout.add(connect);

        TextField textField = new TextField();
        textField.setLabel("Username");
        textField.setPlaceholder("Introdu username...");
        rightLayout.add(textField);

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Parola");
        passwordField.setPlaceholder("Introdu parola...");
        rightLayout.add(passwordField);

        AnnotationConfigApplicationContext contextCustomer = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        CustomerDAO customerDAO = contextCustomer.getBean(CustomerDAO.class);
        contextCustomer.close();

        Button logInButton = new Button("Log in", e -> {
            List<Customer> customerList = customerDAO.getCustomerByUsername(textField.getValue());
            if(customerList.size() == 0 || !customerList.get(0).getPassword().equals(passwordField.getValue())){
                Dialog dialogMessage = new Dialog();
                VerticalLayout dialogMessageV = createDialogMessage(dialogMessage, "Username sau parola incorecte");
                dialogMessage.add(dialogMessageV);
                dialogMessage.open();
            }
            else{
                if(customerList.get(0).getUserType() == 0){
                    Dialog dialogMessage = new Dialog();
                    VerticalLayout dialogMessageV = createDialogMessage(dialogMessage, "Client neinregistrat. Creeaza un cont de client");
                    dialogMessage.add(dialogMessageV);
                    dialogMessage.open();
                }
                else{
                    this.customer = customerList.get(0);
                    dialogLogIn.close();
                }
            }
        });
        logInButton.getElement().getStyle().set("background","#f9003a");
        logInButton.getElement().getStyle().set("color","#ffffff");
        logInButton.getElement().getStyle().set("font-size","15px");
        logInButton.getElement().getStyle().set("font-family","Rockwell");

        Button logInAdminButton = new Button("Log in ca admin", e -> {
            List<Customer> customerList = customerDAO.getCustomerByUsername(textField.getValue());
            if(customerList.size() == 0 || !customerList.get(0).getPassword().equals(passwordField.getValue())){
                Dialog dialogMessage = new Dialog();
                VerticalLayout dialogMessageV = createDialogMessage(dialogMessage, "Username sau parola incorecte");
                dialogMessage.add(dialogMessageV);
                dialogMessage.open();
            }
            else{
                if(customerList.get(0).getUserType() < 2){
                    Dialog dialogMessage = new Dialog();
                    VerticalLayout dialogMessageV = createDialogMessage(dialogMessage, "Nu aveti privilegii de admin!");
                    dialogMessage.add(dialogMessageV);
                    dialogMessage.open();
                }
                else{
                    this.customer = customerList.get(0);
                    Dialog dialogAdmin = new Dialog();
                    HorizontalLayout dialogAdminH = createDialogLayoutAdmin(dialogAdmin);
                    dialogAdmin.add(dialogAdminH);
                    dialogAdmin.open();
                }
            }
        });
        logInAdminButton.getElement().getStyle().set("background","#f9003a");
        logInAdminButton.getElement().getStyle().set("color","#ffffff");
        logInAdminButton.getElement().getStyle().set("font-size","15px");
        logInAdminButton.getElement().getStyle().set("font-family","Rockwell");

        Button logInAdminGeneralButton = new Button("Log in ca admin general", e -> {
            List<Customer> customerList = customerDAO.getCustomerByUsername(textField.getValue());
            if(customerList.size() == 0 || !customerList.get(0).getPassword().equals(passwordField.getValue())){
                Dialog dialogMessage = new Dialog();
                VerticalLayout dialogMessageV = createDialogMessage(dialogMessage, "Username sau parola incorecte");
                dialogMessage.add(dialogMessageV);
                dialogMessage.open();
            }
            else{
                if(customerList.get(0).getUserType() < 3){
                    Dialog dialogMessage = new Dialog();
                    VerticalLayout dialogMessageV = createDialogMessage(dialogMessage, "Nu aveti privilegii de admin general!");
                    dialogMessage.add(dialogMessageV);
                    dialogMessage.open();
                }
                else {
                    this.customer = customerList.get(0);
                    Dialog dialogAdminGeneral = new Dialog();
                    HorizontalLayout dialogAdminGeneralH = createDialogLayoutAdminGeneral(dialogAdminGeneral);
                    dialogAdminGeneral.add(dialogAdminGeneralH);
                    dialogAdminGeneral.open();
                }
            }
        });
        logInAdminGeneralButton.getElement().getStyle().set("background","#f9003a");
        logInAdminGeneralButton.getElement().getStyle().set("color","#ffffff");
        logInAdminGeneralButton.getElement().getStyle().set("font-size","15px");
        logInAdminGeneralButton.getElement().getStyle().set("font-family","Rockwell");

        rightLayout.add(logInButton);
        rightLayout.add(logInAdminButton);
        rightLayout.add(logInAdminGeneralButton);

        Label registerLabel = new Label("Nu ai încă un cont?");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        rightLayout.add(registerLabel);

        Dialog dialogSignIn = new Dialog();
        VerticalLayout dialogLayoutSignIn = createDialogLayoutSignIn(dialogSignIn);
        dialogSignIn.add(dialogLayoutSignIn);

        Button registerButton = new Button("Înregistrează-te", e -> dialogSignIn.open());
        registerButton.getElement().getStyle().set("background","#f9003a");
        registerButton.getElement().getStyle().set("color","#ffffff");
        registerButton.getElement().getStyle().set("font-size","15px");
        registerButton.getElement().getStyle().set("font-family","Rockwell");
        rightLayout.add(registerButton);

        rightLayout.setAlignItems(Alignment.CENTER);

        dialogLayout.add(leftLayout);
        dialogLayout.add(rightLayout);

        return dialogLayout;
    }

    private VerticalLayout createDialogLayoutSignIn(Dialog dialogSignIn) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        HorizontalLayout topWholeLayout = new HorizontalLayout();
        Image image = new Image("https://www.kindpng.com/picc/m/59-592326_cinema-popcorn-png-movie-popcorn-transparent-background-png.png", "signIn");
        image.setHeight(200, Unit.PIXELS);
        topWholeLayout.add(image);


        VerticalLayout topLayout = new VerticalLayout();

        Label pageTitle = new Label("FOCUS");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        topLayout.add(pageTitle);

        Label pageTitle1 = new Label("CINEMA");
        pageTitle1.getElement().getStyle().set("fontWeight","bold");
        pageTitle1.getElement().getStyle().set("font-size","50px");
        pageTitle1.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle1.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle1.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle1.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle1.getElement().getStyle().set("margin-left", "50px");
        topLayout.add(pageTitle1);

        topLayout.setPadding(false);
        topLayout.setMargin(false);
        topWholeLayout.setPadding(false);
        topWholeLayout.add(topLayout);
        dialogLayout.add(topWholeLayout);

        Label registerLabel = new Label("Înregistrează-te");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        dialogLayout.add(registerLabel);

        TextField name = new TextField();
        name.setLabel("Nume");
        name.setPlaceholder("Nume...");
        dialogLayout.add(name);

        TextField firstName = new TextField();
        firstName.setLabel("Prenume");
        firstName.setPlaceholder("Prenume...");
        dialogLayout.add(firstName);

        TextField username = new TextField();
        username.setLabel("Username");
        username.setPlaceholder("Nume de utilizator...");
        dialogLayout.add(username);

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Parola");
        passwordField.setPlaceholder("Introdu o parola...");
        dialogLayout.add(passwordField);

        PasswordField passwordField1 = new PasswordField();
        passwordField1.setLabel("Confirmă");
        passwordField1.setPlaceholder("Confirma parola...");
        dialogLayout.add(passwordField1);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
        context.close();

        Button signInButton = new Button("Înregistrează-te", e -> customerDAO.createCustomer(new Customer(username.getValue(), passwordField.getValue(), firstName.getValue(), name.getValue(), 1)));
        signInButton.getElement().getStyle().set("background","#f9003a");
        signInButton.getElement().getStyle().set("color","#ffffff");
        signInButton.getElement().getStyle().set("font-size","15px");
        signInButton.getElement().getStyle().set("font-family","Rockwell");

        Button cancelButton = new Button("Renunță", e -> dialogSignIn.close());
        cancelButton.getElement().getStyle().set("background","#f9003a");
        cancelButton.getElement().getStyle().set("color","#ffffff");
        cancelButton.getElement().getStyle().set("font-size","15px");
        cancelButton.getElement().getStyle().set("font-family","Rockwell");

        dialogLayout.add(signInButton);
        dialogLayout.add(cancelButton);
        dialogLayout.setAlignItems(Alignment.CENTER);

        return dialogLayout;
    }

    private VerticalLayout createDialogLayout(Dialog dialog, Movie movie) {
        Label pageTitle = new Label(movie.getTitle());
        pageTitle.getElement().getStyle().set("font-size","30px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");

        IFrame iFrame = new IFrame(movie.getTrailerURL());
        iFrame.setHeight("285px");
        iFrame.setWidth("500px");
        iFrame.setAllow("accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture");
        iFrame.getElement().setAttribute("allowfullscreen", true);
        iFrame.getElement().setAttribute("frameborder", "0");

        TextArea movieDescription = new TextArea("Plot");
        movieDescription.setValue(movie.getDescription());
        movieDescription.getElement().getStyle().set("font-family","Rockwell");
        movieDescription.getElement().getStyle().set("color","#f9003a");


        TextArea cast = new TextArea();
        cast.setLabel("Cast");
        cast.setValue(movie.getCast());
        cast.getElement().getStyle().set("font-family","Rockwell");
        cast.getElement().getStyle().set("color","#f9003a");

        TextArea director = new TextArea();
        director.setLabel("Director");
        director.setValue(movie.getDirector());
        director.getElement().getStyle().set("font-family","Rockwell");
        director.getElement().getStyle().set("color","#f9003a");

        TextArea writer = new TextArea();
        writer.setLabel("Writer");
        writer.setValue(movie.getWriter());
        writer.getElement().getStyle().set("font-family","Rockwell");
        writer.getElement().getStyle().set("color","#f9003a");

        TextArea genre = new TextArea();
        genre.setLabel("Genre");
        genre.setValue(movie.getGenre());
        genre.getElement().getStyle().set("font-family","Rockwell");
        genre.getElement().getStyle().set("color","#f9003a");

        TextArea rating = new TextArea();
        rating.setLabel("Rating");
        rating.setValue(movie.getRating().toString());
        rating.getElement().getStyle().set("font-family","Rockwell");
        rating.getElement().getStyle().set("color","#f9003a");

        HorizontalLayout specifications = new HorizontalLayout();
        specifications.getElement().getStyle().set("margin-top", "20px");

        Image image = new Image("https://image.similarpng.com/very-thumbnail/2020/07/Flat-time-icon-clock-vector-PNG.png", "duration");
        image.setHeight(20, Unit.PIXELS);
        image.getElement().getStyle().set("margin-top", "5px");
        image.getElement().getStyle().set("margin-left", "98px");
        specifications.add(image);

        Label duration = new Label(movie.getDuration());
        duration.getElement().getStyle().set("font-family","Rockwell");
        duration.getElement().getStyle().set("font-size","20px");
        duration.getElement().getStyle().set("color","#f9003a");
        specifications.add(duration);

        Label classification = new Label(movie.getClassification());
        classification.getElement().getStyle().set("font-family","Rockwell");
        classification.getElement().getStyle().set("font-size","20px");
        classification.getElement().getStyle().set("color","#f9003a");
        classification.getElement().getStyle().set("margin-left","130px");
        specifications.add(classification);

        Button programme = new Button("Vezi programul", e -> {
            Dialog dialogProgramme = new Dialog();

            HorizontalLayout dialogProgrammeH = createDialogLayoutMovieProgramme(dialogProgramme, movie);
            dialogProgramme.add(dialogProgrammeH);
            dialogProgramme.open();
        });
        programme.getElement().getStyle().set("background","#f9003a");
        programme.getElement().getStyle().set("color","#ffffff");
        programme.getElement().getStyle().set("font-size","15px");
        programme.getElement().getStyle().set("font-family","Rockwell");

        VerticalLayout programmeLayout = new VerticalLayout();
        programmeLayout.setAlignItems(Alignment.CENTER);
        programmeLayout.add(programme);

        VerticalLayout fieldLayout = new VerticalLayout();
        fieldLayout.add(pageTitle);
        fieldLayout.add(iFrame);
        fieldLayout.add(movieDescription);
        fieldLayout.add(cast);
        fieldLayout.add(director);
        fieldLayout.add(writer);
        fieldLayout.add(genre);
        fieldLayout.add(rating);
        fieldLayout.add(specifications);
        fieldLayout.add(programmeLayout);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        VerticalLayout dialogLayout = new VerticalLayout(pageTitle, fieldLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "500px").set("height", "800px").set("max-width", "100%");
        return dialogLayout;
    }

    private static VerticalLayout createDialogLayoutBooking(Dialog dialog, Representation representation, Customer customer) {

        AnnotationConfigApplicationContext context1 = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        MovieDAO movieDAO = context1.getBean(MovieDAO.class);
        TheatreDAO theatreDAO = context1.getBean(TheatreDAO.class);
        ScreenDAO screenDAO = context1.getBean(ScreenDAO.class);
        Screen screen1 = screenDAO.getScreenById(representation.getScreen());
        Movie movie = movieDAO.getMovieById(representation.getMovie());
        context1.close();

        Label pageTitle = new Label(movie.getTitle() + " - " + representation.getStartTime() + "    " + "astazi" + "    -   " + representation.getCinemaLocation(theatreDAO, screenDAO, representation.getScreen()));
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","30px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");

        Label select = new Label("Selectati locurile dorite");
        select.getElement().getStyle().set("font-size","20px");
        select.getElement().getStyle().set("font-family","Rockwell");
        select.getElement().getStyle().set("color","#000000");

        Label seatsLabel = new Label();
        seatsLabel.getElement().getStyle().set("font-size","20px");
        seatsLabel.getElement().getStyle().set("font-family","Rockwell");
        seatsLabel.getElement().getStyle().set("color","#000000");
        seatsLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        seatsLabel.getElement().getStyle().set("-webkit-background-clip","text");
        seatsLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");

        Label screen = new Label("________________________ecran________________________");
        screen.getElement().getStyle().set("text-align", "center");
        screen.getElement().getStyle().set("font-size","30px");
        screen.getElement().getStyle().set("font-family","Rockwell");
        screen.getElement().getStyle().set("color","#000000");

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        BookingDAO bookingDAO = context.getBean(BookingDAO.class);
        TicketDAO ticketDAO = context.getBean(TicketDAO.class);
        RepresentationDAO representationDAO = context.getBean(RepresentationDAO.class);
        CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
        PriceDAO priceDAO = context.getBean(PriceDAO.class);
        context.close();

        List<Booking> bookingList = bookingDAO.getAllBookingsRepresentation(representationDAO.getId(representation));
        List<Ticket> ticketList = ticketDAO.getAllTicketsRepresentation(representationDAO.getId(representation));
        List<Seat> seatsCoordinates = new ArrayList<>();

        VerticalLayout seats = new VerticalLayout();
        for(int i = 1; i <= screen1.getNbRows(); i++){
            HorizontalLayout row = new HorizontalLayout();
            for(int j = 1; j <= screen1.getNbChairsRow(); j++){
                int finalI1 = i;
                int finalJ1 = j;
                Button seat = new Button(i + "/" + j, e -> {
                    Seat thisSeat = new Seat(finalI1, finalJ1);
                    List <Seat> found = seatsCoordinates.stream().filter( x -> x.getRow() == thisSeat.getRow() && x.getCol() == thisSeat.getCol()).collect(Collectors.toList());
                    if(found.size() > 0){
                        seatsCoordinates.remove(found.get(0));
                        seatsLabel.setText("");
                        if(seatsCoordinates.size() > 0){
                            for(int k = 0; k < seatsCoordinates.size(); k ++){
                                if(k == 0)
                                    seatsLabel.add(seatsCoordinates.get(0).getRow() + "/" + seatsCoordinates.get(0).getCol());
                                else
                                    seatsLabel.add(",  " + seatsCoordinates.get(k).getRow() + "/" + seatsCoordinates.get(k).getCol());
                            }
                        }
                    }
                    else{
                        seatsCoordinates.add(thisSeat);
                        if(seatsCoordinates.size() > 0){
                            if(seatsCoordinates.size() == 1)
                                seatsLabel.add(seatsCoordinates.get(0).getRow() + "/" + seatsCoordinates.get(0).getCol());
                            else
                                seatsLabel.add(",  " + seatsCoordinates.get(seatsCoordinates.size() - 1).getRow() + "/" + seatsCoordinates.get(seatsCoordinates.size() - 1).getCol());
                        }
                    }
                });
                seat.getElement().getStyle().set("color","#000000");
                seat.getElement().getStyle().set("font-size","14px");
                seat.getElement().getStyle().set("font-family","Rockwell");
                seat.getElement().getStyle().set("background","#53cad7");
                int finalI = i;
                int finalJ = j;
                if(bookingList.stream().filter(x -> x.getRow() == finalI).filter(x -> x.getSeat() == finalJ).count() == 1 ||
                        ticketList.stream().filter(x -> x.getRow() == finalI).filter(x -> x.getSeat() == finalJ).count() == 1) {
                    seat.getElement().getStyle().set("background", "#A9A9A9");
                    seat.setEnabled(false);
                }
                row.add(seat);
            }
            seats.add(row);
        }

        VerticalLayout buttons = new VerticalLayout();
        buttons.setAlignItems(Alignment.CENTER);

        Button booking = new Button("Rezervă", e -> {
            for(Seat x : seatsCoordinates){
                Booking booking1 = new Booking(customerDAO.getId(customer.getUsername()), representationDAO.getId(representation), x.getRow(), x.getCol());
                bookingDAO.createBooking(booking1);
            }
        });
        booking.getElement().getStyle().set("background","#f9003a");
        booking.getElement().getStyle().set("color","#ffffff");
        booking.getElement().getStyle().set("font-size","20px");
        booking.getElement().getStyle().set("font-family","Rockwell");

        Button ticket = new Button("Cumpără bilet", e -> {
            for(Seat x : seatsCoordinates){
                Ticket ticket1 = new Ticket(customerDAO.getId(customer.getUsername()), representationDAO.getId(representation), priceDAO.getPriceForTypeOfPlay(representation.getTypeOfPlay()), x.getRow(), x.getCol());
                ticketDAO.createTicket(ticket1);
            }
        });
        ticket.getElement().getStyle().set("background","#f9003a");
        ticket.getElement().getStyle().set("color","#ffffff");
        ticket.getElement().getStyle().set("font-size","20px");
        ticket.getElement().getStyle().set("font-family","Rockwell");
        buttons.add(booking);
        buttons.add(ticket);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(pageTitle);
        dialogLayout.add(select);
        dialogLayout.add(seatsLabel);
        dialogLayout.add(screen);
        dialogLayout.add(seats);
        dialogLayout.add(buttons);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "900px").set("height", "900px").set("max-width", "100%");
        return dialogLayout;
    }

    private static VerticalLayout createDialogMessage(Dialog dialog, String message) {

        Label pageTitle = new Label(message);
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","30px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle.getElement().getStyle().set("text-align", "center");

        VerticalLayout buttons = new VerticalLayout();
        buttons.setAlignItems(Alignment.CENTER);

        Button ok = new Button("OK", e -> dialog.close());
        ok.getElement().getStyle().set("background","#f9003a");
        ok.getElement().getStyle().set("color","#ffffff");
        ok.getElement().getStyle().set("font-size","20px");
        ok.getElement().getStyle().set("font-family","Rockwell");
        buttons.add(ok);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(pageTitle);
        dialogLayout.add(buttons);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("height", "200px").set("max-width", "100%");
        return dialogLayout;
    }

    private HorizontalLayout createDialogLayoutAdmin(Dialog dialogAdmin) {
        HorizontalLayout dialogLayout = new HorizontalLayout();
        dialogLayout.setPadding(false);

        VerticalLayout leftLayout = new VerticalLayout();
        Image image = new Image("https://w7.pngwing.com/pngs/926/948/png-transparent-popcorn-discount-theater-film-cinema-movie-theatre-computer-wallpaper-film-poster-stock-photography.png", "dialogAdmin");
        image.setHeight(300, Unit.PIXELS);
        leftLayout.add(image);

        Label pageTitle = new Label("FOCUS CINEMA");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        leftLayout.add(pageTitle);

        VerticalLayout rightLayout = new VerticalLayout();

        Label empty = new Label("");
        empty.setHeight(25, Unit.PIXELS);
        rightLayout.add(empty);

        Label connect = new Label("Bine ai venit, admin!");
        connect.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        connect.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        connect.getElement().getStyle().set("-webkit-background-clip","text");
        connect.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        rightLayout.add(connect);

        Button addRepresentation = new Button("Adauga rulare", e -> {
            Dialog dialogAddRepresentation = new Dialog();
            VerticalLayout dialogAddRepresentationV = createDialogLayoutAddRepresentation(dialogAddRepresentation);
            dialogAddRepresentation.add(dialogAddRepresentationV);
            dialogAddRepresentation.open();
        });
        addRepresentation.getElement().getStyle().set("background","#f9003a");
        addRepresentation.getElement().getStyle().set("color","#ffffff");
        addRepresentation.getElement().getStyle().set("font-size","15px");
        addRepresentation.getElement().getStyle().set("font-family","Rockwell");

        Button modifyRepresentation = new Button("Modifica rulare", e -> {
            Dialog dialogModifyRepresentation = new Dialog();
            VerticalLayout dialogModifyRepresentationV = createDialogModifyRepresentation(dialogModifyRepresentation);
            dialogModifyRepresentation.add(dialogModifyRepresentationV);
            dialogModifyRepresentation.open();
        });
        modifyRepresentation.getElement().getStyle().set("background","#f9003a");
        modifyRepresentation.getElement().getStyle().set("color","#ffffff");
        modifyRepresentation.getElement().getStyle().set("font-size","15px");
        modifyRepresentation.getElement().getStyle().set("font-family","Rockwell");

        Button deleteRepresentation = new Button("Elimina rulare", e -> {
            Dialog dialogDeleteRepresentation = new Dialog();
            VerticalLayout dialogDeleteRepresentationV = createDialogDeleteRepresentation(dialogDeleteRepresentation);
            dialogDeleteRepresentation.add(dialogDeleteRepresentationV);
            dialogDeleteRepresentation.open();
        });
        deleteRepresentation.getElement().getStyle().set("background","#f9003a");
        deleteRepresentation.getElement().getStyle().set("color","#ffffff");
        deleteRepresentation.getElement().getStyle().set("font-size","15px");
        deleteRepresentation.getElement().getStyle().set("font-family","Rockwell");

        Button addCustomer = new Button("Adauga client", e -> {
            Dialog dialogAddClient = new Dialog();
            VerticalLayout dialogAddClientV = createDialogLayoutAddClient(dialogAddClient);
            dialogAddClient.add(dialogAddClientV);
            dialogAddClient.open();
        });
        addCustomer.getElement().getStyle().set("background","#f9003a");
        addCustomer.getElement().getStyle().set("color","#ffffff");
        addCustomer.getElement().getStyle().set("font-size","15px");
        addCustomer.getElement().getStyle().set("font-family","Rockwell");

        rightLayout.add(addRepresentation);
        rightLayout.add(modifyRepresentation);
        rightLayout.add(deleteRepresentation);
        rightLayout.add(addCustomer);

        rightLayout.setAlignItems(Alignment.CENTER);

        dialogLayout.add(leftLayout);
        dialogLayout.add(rightLayout);
        dialogLayout.setAlignItems(Alignment.STRETCH);
        return dialogLayout;
    }

    private HorizontalLayout createDialogLayoutAdminGeneral(Dialog dialogAdminGeneral) {
        HorizontalLayout dialogLayout = new HorizontalLayout();
        dialogLayout.setPadding(false);

        VerticalLayout leftLayout = new VerticalLayout();
        Image image = new Image("https://static.timesofisrael.com/blogs/uploads/2019/03/Cinema-popcorn-671104010-1.jpg", "dialogAdminGeneral");
        image.setHeight(300, Unit.PIXELS);
        leftLayout.add(image);

        Label pageTitle = new Label("FOCUS CINEMA");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        leftLayout.add(pageTitle);

        VerticalLayout rightLayout = new VerticalLayout();

        Label connect = new Label("Bine ai venit, admin general!");
        connect.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        connect.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        connect.getElement().getStyle().set("-webkit-background-clip","text");
        connect.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        rightLayout.add(connect);

        Button addMovie = new Button("Adauga film", e -> {
            Dialog dialogAddMovie = new Dialog();
            VerticalLayout dialogAddMovieV = createDialogLayoutAddMovie(dialogAddMovie);
            dialogAddMovie.add(dialogAddMovieV);
            dialogAddMovie.open();
        });
        addMovie.getElement().getStyle().set("background","#f9003a");
        addMovie.getElement().getStyle().set("color","#ffffff");
        addMovie.getElement().getStyle().set("font-size","15px");
        addMovie.getElement().getStyle().set("font-family","Rockwell");

        Button modifyMovie = new Button("Modifica film", e -> {
            Dialog dialogModifyMovie = new Dialog();
            VerticalLayout dialogModifyMovieV = createDialogModifyMovie(dialogModifyMovie);
            dialogModifyMovie.add(dialogModifyMovieV);
            dialogModifyMovie.open();
        });
        modifyMovie.getElement().getStyle().set("background","#f9003a");
        modifyMovie.getElement().getStyle().set("color","#ffffff");
        modifyMovie.getElement().getStyle().set("font-size","15px");
        modifyMovie.getElement().getStyle().set("font-family","Rockwell");

        Button deleteMovie = new Button("Elimina film", e -> {
            Dialog dialogDeleteMovie = new Dialog();
            VerticalLayout dialogDeleteMovieV = createDialogDeleteMovie(dialogDeleteMovie);
            dialogDeleteMovie.add(dialogDeleteMovieV);
            dialogDeleteMovie.open();
        });
        deleteMovie.getElement().getStyle().set("background","#f9003a");
        deleteMovie.getElement().getStyle().set("color","#ffffff");
        deleteMovie.getElement().getStyle().set("font-size","15px");
        deleteMovie.getElement().getStyle().set("font-family","Rockwell");

        Button addCustomer = new Button("Adauga admin", e -> {
            Dialog dialogAddAdmin = new Dialog();
            VerticalLayout dialogAddAdminV = createDialogLayoutAddAdmin(dialogAddAdmin);
            dialogAddAdmin.add(dialogAddAdminV);
            dialogAddAdmin.open();
        });
        addCustomer.getElement().getStyle().set("background","#f9003a");
        addCustomer.getElement().getStyle().set("color","#ffffff");
        addCustomer.getElement().getStyle().set("font-size","15px");
        addCustomer.getElement().getStyle().set("font-family","Rockwell");

        Button modifyTicketPrice = new Button("Modifica pret bilete", e -> {
            Dialog dialogModifyPrice = new Dialog();
            VerticalLayout dialogModifyPriceV = createDialogModifyPrice(dialogModifyPrice);
            dialogModifyPrice.add(dialogModifyPriceV);
            dialogModifyPrice.open();
        });
        modifyTicketPrice.getElement().getStyle().set("background","#f9003a");
        modifyTicketPrice.getElement().getStyle().set("color","#ffffff");
        modifyTicketPrice.getElement().getStyle().set("font-size","15px");
        modifyTicketPrice.getElement().getStyle().set("font-family","Rockwell");

        rightLayout.add(addMovie);
        rightLayout.add(modifyMovie);
        rightLayout.add(deleteMovie);
        rightLayout.add(addCustomer);
        rightLayout.add(modifyTicketPrice);

        rightLayout.setAlignItems(Alignment.CENTER);

        dialogLayout.add(leftLayout);
        dialogLayout.add(rightLayout);
        dialogLayout.setAlignItems(Alignment.STRETCH);
        return dialogLayout;
    }

    private VerticalLayout createDialogLayoutCog(Dialog dialogCog, Customer customer){
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        HorizontalLayout topWholeLayout = new HorizontalLayout();
        Image image = new Image("https://freedesignfile.com/upload/2018/01/Cinema-background-with-popcorn-snacks-vector-01.jpg", "cog");
        image.setHeight(200, Unit.PIXELS);
        topWholeLayout.add(image);


        VerticalLayout topLayout = new VerticalLayout();

        Label pageTitle = new Label("FOCUS");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        topLayout.add(pageTitle);

        Label pageTitle1 = new Label("CINEMA");
        pageTitle1.getElement().getStyle().set("fontWeight","bold");
        pageTitle1.getElement().getStyle().set("font-size","50px");
        pageTitle1.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle1.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle1.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle1.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle1.getElement().getStyle().set("margin-left", "50px");
        topLayout.add(pageTitle1);

        topLayout.setPadding(false);
        topLayout.setMargin(false);
        topWholeLayout.setPadding(false);
        topWholeLayout.add(topLayout);
        dialogLayout.add(topWholeLayout);

        Label registerLabel = new Label("Contul meu");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        dialogLayout.add(registerLabel);

        TextField username = new TextField();
        username.setLabel("Username");
        username.setPlaceholder("Nume de utilizator...");
        username.setValue(customer.getUsername());
        username.setEnabled(false);
        dialogLayout.add(username);

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Parola");
        passwordField.setPlaceholder("Introdu o parola...");
        passwordField.setValue(customer.getPassword());
        dialogLayout.add(passwordField);

        TextField name = new TextField();
        name.setLabel("Nume");
        name.setPlaceholder("Nume...");
        name.setValue(customer.getLastName());
        dialogLayout.add(name);

        TextField firstName = new TextField();
        firstName.setLabel("Prenume");
        firstName.setPlaceholder("Prenume...");
        firstName.setValue(customer.getFirstName());
        dialogLayout.add(firstName);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
        context.close();

        Button signInButton = new Button("Actualizeaza datele", e -> customerDAO.updateCustomer(new Customer(username.getValue(), passwordField.getValue(), firstName.getValue(), name.getValue())));
        signInButton.getElement().getStyle().set("background","#f9003a");
        signInButton.getElement().getStyle().set("color","#ffffff");
        signInButton.getElement().getStyle().set("font-size","15px");
        signInButton.getElement().getStyle().set("font-family","Rockwell");

        Button cancelButton = new Button("Sterge cont", e -> {
            customerDAO.deleteCustomer(this.customer);
            this.customer = new Customer();
        });
        cancelButton.getElement().getStyle().set("background","#f9003a");
        cancelButton.getElement().getStyle().set("color","#ffffff");
        cancelButton.getElement().getStyle().set("font-size","15px");
        cancelButton.getElement().getStyle().set("font-family","Rockwell");

        dialogLayout.add(signInButton);
        dialogLayout.add(cancelButton);
        dialogLayout.setAlignItems(Alignment.CENTER);

        return dialogLayout;
    }

    private VerticalLayout createDialogLayoutAddClient(Dialog dialogAddClient) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        HorizontalLayout topWholeLayout = new HorizontalLayout();
        Image image = new Image("https://kingkalli.de/wp-content/uploads/2019/11/kinoangebote_kinder_aachen.jpg", "sorry :(");
        image.setHeight(200, Unit.PIXELS);
        topWholeLayout.add(image);


        VerticalLayout topLayout = new VerticalLayout();

        Label pageTitle = new Label("FOCUS");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        topLayout.add(pageTitle);

        Label pageTitle1 = new Label("CINEMA");
        pageTitle1.getElement().getStyle().set("fontWeight","bold");
        pageTitle1.getElement().getStyle().set("font-size","50px");
        pageTitle1.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle1.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle1.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle1.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle1.getElement().getStyle().set("margin-left", "50px");
        topLayout.add(pageTitle1);

        topLayout.setPadding(false);
        topLayout.setMargin(false);
        topWholeLayout.setPadding(false);
        topWholeLayout.add(topLayout);
        dialogLayout.add(topWholeLayout);

        Label registerLabel = new Label("Adauga client");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        dialogLayout.add(registerLabel);

        TextField name = new TextField();
        name.setLabel("Nume");
        name.setPlaceholder("Nume...");
        dialogLayout.add(name);

        TextField firstName = new TextField();
        firstName.setLabel("Prenume");
        firstName.setPlaceholder("Prenume...");
        dialogLayout.add(firstName);

        TextField username = new TextField();
        username.setLabel("Username");
        username.setPlaceholder("Nume de utilizator...");
        dialogLayout.add(username);

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Parola");
        passwordField.setPlaceholder("Introdu o parola...");
        dialogLayout.add(passwordField);

        PasswordField passwordField1 = new PasswordField();
        passwordField1.setLabel("Confirmă");
        passwordField1.setPlaceholder("Confirma parola...");
        dialogLayout.add(passwordField1);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
        context.close();

        Button signInButton = new Button("Adauga", e -> customerDAO.createCustomer(new Customer(username.getValue(), passwordField.getValue(), firstName.getValue(), name.getValue(), 1)));
        signInButton.getElement().getStyle().set("background","#f9003a");
        signInButton.getElement().getStyle().set("color","#ffffff");
        signInButton.getElement().getStyle().set("font-size","15px");
        signInButton.getElement().getStyle().set("font-family","Rockwell");

        Button cancelButton = new Button("Renunță", e -> dialogAddClient.close());
        cancelButton.getElement().getStyle().set("background","#f9003a");
        cancelButton.getElement().getStyle().set("color","#ffffff");
        cancelButton.getElement().getStyle().set("font-size","15px");
        cancelButton.getElement().getStyle().set("font-family","Rockwell");

        dialogLayout.add(signInButton);
        dialogLayout.add(cancelButton);
        dialogLayout.setAlignItems(Alignment.CENTER);

        return dialogLayout;
    }

    private VerticalLayout createDialogLayoutAddAdmin(Dialog dialogAddAdmin) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        HorizontalLayout topWholeLayout = new HorizontalLayout();
        Image image = new Image("https://image.freepik.com/vrije-vector/isometrische-cinema-icon-set_1284-18691.jpg", "sorry :(");
        image.setHeight(200, Unit.PIXELS);
        topWholeLayout.add(image);


        VerticalLayout topLayout = new VerticalLayout();

        Label pageTitle = new Label("FOCUS");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        topLayout.add(pageTitle);

        Label pageTitle1 = new Label("CINEMA");
        pageTitle1.getElement().getStyle().set("fontWeight","bold");
        pageTitle1.getElement().getStyle().set("font-size","50px");
        pageTitle1.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle1.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle1.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle1.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle1.getElement().getStyle().set("margin-left", "50px");
        topLayout.add(pageTitle1);

        topLayout.setPadding(false);
        topLayout.setMargin(false);
        topWholeLayout.setPadding(false);
        topWholeLayout.add(topLayout);
        dialogLayout.add(topWholeLayout);

        Label registerLabel = new Label("Adauga admin");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        dialogLayout.add(registerLabel);

        TextField name = new TextField();
        name.setLabel("Nume");
        name.setPlaceholder("Nume...");
        dialogLayout.add(name);

        TextField firstName = new TextField();
        firstName.setLabel("Prenume");
        firstName.setPlaceholder("Prenume...");
        dialogLayout.add(firstName);

        TextField username = new TextField();
        username.setLabel("Username");
        username.setPlaceholder("Nume de utilizator...");
        dialogLayout.add(username);

        PasswordField passwordField = new PasswordField();
        passwordField.setLabel("Parola");
        passwordField.setPlaceholder("Introdu o parola...");
        dialogLayout.add(passwordField);

        PasswordField passwordField1 = new PasswordField();
        passwordField1.setLabel("Confirmă");
        passwordField1.setPlaceholder("Confirma parola...");
        dialogLayout.add(passwordField1);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        CustomerDAO customerDAO = context.getBean(CustomerDAO.class);
        context.close();

        Button signInButton = new Button("Adauga", e -> customerDAO.createCustomer(new Customer(username.getValue(), passwordField.getValue(), firstName.getValue(), name.getValue(), 2)));
        signInButton.getElement().getStyle().set("background","#f9003a");
        signInButton.getElement().getStyle().set("color","#ffffff");
        signInButton.getElement().getStyle().set("font-size","15px");
        signInButton.getElement().getStyle().set("font-family","Rockwell");

        Button cancelButton = new Button("Renunță", e -> dialogAddAdmin.close());
        cancelButton.getElement().getStyle().set("background","#f9003a");
        cancelButton.getElement().getStyle().set("color","#ffffff");
        cancelButton.getElement().getStyle().set("font-size","15px");
        cancelButton.getElement().getStyle().set("font-family","Rockwell");

        dialogLayout.add(signInButton);
        dialogLayout.add(cancelButton);
        dialogLayout.setAlignItems(Alignment.CENTER);

        return dialogLayout;
    }

    private static VerticalLayout createDialogLayoutAddMovie(Dialog dialogAddMovie) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        HorizontalLayout topWholeLayout = new HorizontalLayout();
        Image image = new Image("https://assets.telegraphindia.com/telegraph/79bb87f9-611b-4e32-9946-35725aa31402.jpg", "sorry :(");
        image.setHeight(200, Unit.PIXELS);
        topWholeLayout.add(image);


        VerticalLayout topLayout = new VerticalLayout();

        Label pageTitle = new Label("FOCUS");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        topLayout.add(pageTitle);

        Label pageTitle1 = new Label("CINEMA");
        pageTitle1.getElement().getStyle().set("fontWeight","bold");
        pageTitle1.getElement().getStyle().set("font-size","50px");
        pageTitle1.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle1.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle1.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle1.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle1.getElement().getStyle().set("margin-left", "50px");
        topLayout.add(pageTitle1);

        topLayout.setPadding(false);
        topLayout.setMargin(false);
        topWholeLayout.setPadding(false);
        topWholeLayout.add(topLayout);
        dialogLayout.add(topWholeLayout);

        Label registerLabel = new Label("Adauga film");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        dialogLayout.add(registerLabel);

        TextField title = new TextField();
        title.setLabel("Titlu");
        dialogLayout.add(title);

        TextField poster = new TextField();
        poster.setLabel("URL poster");
        dialogLayout.add(poster);

        TextField rating = new TextField();
        rating.setLabel("Rating IMDb");
        dialogLayout.add(rating);

        TextField description = new TextField();
        description.setLabel("Descriere");
        dialogLayout.add(description);

        TextField cast = new TextField();
        cast.setLabel("Distributie");
        dialogLayout.add(cast);

        TextField director = new TextField();
        director.setLabel("Regizor");
        dialogLayout.add(director);

        TextField writer = new TextField();
        writer.setLabel("Scenarist");
        dialogLayout.add(writer);

        TextField genre = new TextField();
        genre.setLabel("Gen");
        dialogLayout.add(genre);

        TextField trailer = new TextField();
        trailer.setLabel("URL trailer");
        dialogLayout.add(trailer);

        TextField duration = new TextField();
        duration.setLabel("Durata");
        dialogLayout.add(duration);

        TextField classification = new TextField();
        classification.setLabel("Clasificare");
        dialogLayout.add(classification);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        MovieDAO movieDAO = context.getBean(MovieDAO.class);
        context.close();

        Button signInButton = new Button("Adauga", e -> movieDAO.createMovie(new Movie(title.getValue(), poster.getValue(), Float.parseFloat(rating.getValue()),
                description.getValue(), cast.getValue(), director.getValue(), writer.getValue(), genre.getValue(), trailer.getValue(), duration.getValue(), classification.getValue())));
        signInButton.getElement().getStyle().set("background","#f9003a");
        signInButton.getElement().getStyle().set("color","#ffffff");
        signInButton.getElement().getStyle().set("font-size","15px");
        signInButton.getElement().getStyle().set("font-family","Rockwell");

        dialogLayout.add(signInButton);
        dialogLayout.setAlignItems(Alignment.CENTER);

        return dialogLayout;
    }

    private static VerticalLayout createDialogLayoutAddRepresentation(Dialog dialogAddRepresentation) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        HorizontalLayout topWholeLayout = new HorizontalLayout();
        Image image = new Image("https://www.richmond950.co.uk/Portals/0/adam/Layout/SykNacpURUqc_ungrIAyqw/Image/52004697_s.jpg", "sorry :(");
        image.setHeight(200, Unit.PIXELS);
        topWholeLayout.add(image);


        VerticalLayout topLayout = new VerticalLayout();

        Label pageTitle = new Label("FOCUS");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        topLayout.add(pageTitle);

        Label pageTitle1 = new Label("CINEMA");
        pageTitle1.getElement().getStyle().set("fontWeight","bold");
        pageTitle1.getElement().getStyle().set("font-size","50px");
        pageTitle1.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle1.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle1.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle1.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle1.getElement().getStyle().set("margin-left", "50px");
        topLayout.add(pageTitle1);

        topLayout.setPadding(false);
        topLayout.setMargin(false);
        topWholeLayout.setPadding(false);
        topWholeLayout.add(topLayout);
        dialogLayout.add(topWholeLayout);

        Label registerLabel = new Label("Adauga rulare");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        dialogLayout.add(registerLabel);

        TextField title = new TextField();
        title.setLabel("Titlul filmului");
        title.setPlaceholder("Titlul original al filmului");
        dialogLayout.add(title);

        TextField dayOfWeek = new TextField();
        dayOfWeek.setLabel("Ziua saptamanii");
        dayOfWeek.setPlaceholder("Lu, Ma, Mi, Jo, Vi, Sa, Du");
        dialogLayout.add(dayOfWeek);

        TextField startTime = new TextField();
        startTime.setLabel("Ora de incepere");
        startTime.setPlaceholder("Ora de la care se va rula");
        dialogLayout.add(startTime);

        TextField screen = new TextField();
        screen.setLabel("Sala");
        screen.setPlaceholder("1, 2, 3, 4...");
        dialogLayout.add(screen);

        TextField cinema = new TextField();
        cinema.setLabel("Cinema");
        cinema.setPlaceholder("Alba Iulia, Cluj-Napoca");
        dialogLayout.add(cinema);

        TextField typeOfPlay = new TextField();
        typeOfPlay.setLabel("Tipul rularii");
        typeOfPlay.setPlaceholder("2D, 3D, 4DX");
        dialogLayout.add(typeOfPlay);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        RepresentationDAO representationDAO = context.getBean(RepresentationDAO.class);
        MovieDAO movieDAO = context.getBean(MovieDAO.class);
        ScreenDAO screenDAO = context.getBean(ScreenDAO.class);
        context.close();


        Button signInButton = new Button("Adauga", e -> representationDAO.insertRepresentation(new Representation(movieDAO.getMovieIdByTitle(title.getValue()),
                toIntDayOfWeek(dayOfWeek.getValue()), startTime.getValue(), screenDAO.getIdOfScreen(cinema.getValue(), screen.getValue()), typeOfPlay.getValue())));
        signInButton.getElement().getStyle().set("background","#f9003a");
        signInButton.getElement().getStyle().set("color","#ffffff");
        signInButton.getElement().getStyle().set("font-size","15px");
        signInButton.getElement().getStyle().set("font-family","Rockwell");

        dialogLayout.add(signInButton);
        dialogLayout.setAlignItems(Alignment.CENTER);

        return dialogLayout;
    }

    private static VerticalLayout createDialogDeleteMovie(Dialog dialog) {

        Label pageTitle = new Label("Elimina film");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","30px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle.getElement().getStyle().set("text-align", "center");

        VerticalLayout buttons = new VerticalLayout();
        buttons.setAlignItems(Alignment.CENTER);

        Grid<Movie> grid = new Grid<>(Movie.class, false);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(Movie::getTitle).setHeader("Movie title");
        grid.addColumn(Movie::getCast).setHeader("Cast");
        grid.addColumn(Movie::getDirector).setHeader("Director");
        grid.addColumn(Movie::getTitle).setHeader("Genre");
        grid.addColumn(Movie::getCast).setHeader("Classification");
        grid.addColumn(Movie::getDirector).setHeader("Duration");


        AnnotationConfigApplicationContext contextMovies = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        MovieDAO movieDAO = contextMovies.getBean(MovieDAO.class);
        List<Movie> movieList = movieDAO.getAllMovies();
        contextMovies.close();

        grid.setItems(movieList);

        Button ok = new Button("Elimina", e -> {
            for(Movie x : grid.getSelectedItems())
                movieDAO.deleteMovie(x);
        });
        ok.getElement().getStyle().set("background","#f9003a");
        ok.getElement().getStyle().set("color","#ffffff");
        ok.getElement().getStyle().set("font-size","20px");
        ok.getElement().getStyle().set("font-family","Rockwell");
        buttons.add(ok);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(pageTitle);
        dialogLayout.add(grid);
        dialogLayout.add(buttons);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "1200px").set("height", "800px").set("max-width", "100%");
        return dialogLayout;
    }

    private static VerticalLayout createDialogDeleteRepresentation(Dialog dialog) {

        Label pageTitle = new Label("Elimina rulare");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","30px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle.getElement().getStyle().set("text-align", "center");

        VerticalLayout buttons = new VerticalLayout();
        buttons.setAlignItems(Alignment.CENTER);

        AnnotationConfigApplicationContext contextMovies = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        RepresentationDAO representationDAO = contextMovies.getBean(RepresentationDAO.class);
        MovieDAO movieDAO = contextMovies.getBean(MovieDAO.class);
        ScreenDAO screenDAO = contextMovies.getBean(ScreenDAO.class);
        TheatreDAO theatreDAO = contextMovies.getBean(TheatreDAO.class);
        List<Representation> representationList = representationDAO.getAllRepresentations();
        contextMovies.close();

        Grid<Representation> grid = new Grid<>(Representation.class, false);
        grid.setSelectionMode(Grid.SelectionMode.MULTI);
        grid.addColumn(r -> r.getMovieTitle(movieDAO, r.getMovie())).setHeader("Movie");
        grid.addColumn(r -> toDayOfWeekInt(r.getDayOfWeek())).setHeader("Day of week");
        grid.addColumn(Representation::getStartTime).setHeader("Start time");
        grid.addColumn(r -> r.getScreenName(screenDAO, r.getScreen())).setHeader("Screen");
        grid.addColumn(r -> r.getCinemaLocation(theatreDAO, screenDAO, r.getScreen())).setHeader("Cinema");
        grid.addColumn(Representation::getTypeOfPlay).setHeader("Type");
        grid.setItems(representationList);

        Button ok = new Button("Elimina", e -> {
            for(Representation x : grid.getSelectedItems())
                representationDAO.deleteRepresentation(x);
        });
        ok.getElement().getStyle().set("background","#f9003a");
        ok.getElement().getStyle().set("color","#ffffff");
        ok.getElement().getStyle().set("font-size","20px");
        ok.getElement().getStyle().set("font-family","Rockwell");
        buttons.add(ok);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(pageTitle);
        dialogLayout.add(grid);
        dialogLayout.add(buttons);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "1200px").set("height", "800px").set("max-width", "100%");
        return dialogLayout;
    }

    private static VerticalLayout createDialogModifyMovie(Dialog dialog) {

        Label pageTitle = new Label("Modifica film");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","30px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle.getElement().getStyle().set("text-align", "center");

        VerticalLayout buttons = new VerticalLayout();
        buttons.setAlignItems(Alignment.CENTER);

        Grid<Movie> grid = new Grid<>(Movie.class, false);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(Movie::getTitle).setHeader("Movie title");
        grid.addColumn(Movie::getCast).setHeader("Cast");
        grid.addColumn(Movie::getDirector).setHeader("Director");
        grid.addColumn(Movie::getTitle).setHeader("Genre");
        grid.addColumn(Movie::getCast).setHeader("Classification");
        grid.addColumn(Movie::getDirector).setHeader("Duration");


        AnnotationConfigApplicationContext contextMovies = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        MovieDAO movieDAO = contextMovies.getBean(MovieDAO.class);
        List<Movie> movieList = movieDAO.getAllMovies();
        contextMovies.close();

        grid.setItems(movieList);

        Button ok = new Button("Modifica", e -> {
            for(Movie x : grid.getSelectedItems()){
                Dialog dialogModifyMovieData = new Dialog();
                VerticalLayout dialogModifyMovieDataV = createDialogLayoutModifyMovieData(dialogModifyMovieData, x);
                dialogModifyMovieData.add(dialogModifyMovieDataV);
                dialogModifyMovieData.open();
            }
        });
        ok.getElement().getStyle().set("background","#f9003a");
        ok.getElement().getStyle().set("color","#ffffff");
        ok.getElement().getStyle().set("font-size","20px");
        ok.getElement().getStyle().set("font-family","Rockwell");
        buttons.add(ok);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(pageTitle);
        dialogLayout.add(grid);
        dialogLayout.add(buttons);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "1200px").set("height", "800px").set("max-width", "100%");
        return dialogLayout;
    }

    private static VerticalLayout createDialogLayoutModifyMovieData(Dialog dialogMovieData, Movie movie) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        HorizontalLayout topWholeLayout = new HorizontalLayout();
        Image image = new Image("https://www.trendingus.com/wp-content/uploads/2021/05/fbaf292a2e24086170ee44a149d2de2e.jpeg", "sorry :(");
        image.setHeight(200, Unit.PIXELS);
        topWholeLayout.add(image);


        VerticalLayout topLayout = new VerticalLayout();

        Label pageTitle = new Label("FOCUS");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        topLayout.add(pageTitle);

        Label pageTitle1 = new Label("CINEMA");
        pageTitle1.getElement().getStyle().set("fontWeight","bold");
        pageTitle1.getElement().getStyle().set("font-size","50px");
        pageTitle1.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle1.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle1.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle1.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle1.getElement().getStyle().set("margin-left", "50px");
        topLayout.add(pageTitle1);

        topLayout.setPadding(false);
        topLayout.setMargin(false);
        topWholeLayout.setPadding(false);
        topWholeLayout.add(topLayout);
        dialogLayout.add(topWholeLayout);

        Label registerLabel = new Label("Modifica film");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        dialogLayout.add(registerLabel);

        TextField title = new TextField();
        title.setLabel("Titlu");
        title.setValue(movie.getTitle());
        dialogLayout.add(title);

        TextField poster = new TextField();
        poster.setLabel("URL poster");
        poster.setValue(movie.getPoster());
        dialogLayout.add(poster);

        TextField rating = new TextField();
        rating.setLabel("Rating IMDb");
        rating.setValue(movie.getRating().toString());
        dialogLayout.add(rating);

        TextField description = new TextField();
        description.setLabel("Descriere");
        description.setValue(movie.getDescription());
        dialogLayout.add(description);

        TextField cast = new TextField();
        cast.setLabel("Distributie");
        cast.setValue(movie.getCast());
        dialogLayout.add(cast);

        TextField director = new TextField();
        director.setLabel("Regizor");
        director.setValue(movie.getDirector());
        dialogLayout.add(director);

        TextField writer = new TextField();
        writer.setLabel("Scenarist");
        writer.setValue(movie.getWriter());
        dialogLayout.add(writer);

        TextField genre = new TextField();
        genre.setLabel("Gen");
        genre.setValue(movie.getGenre());
        dialogLayout.add(genre);

        TextField trailer = new TextField();
        trailer.setLabel("URL trailer");
        trailer.setValue(movie.getTrailerURL());
        dialogLayout.add(trailer);

        TextField duration = new TextField();
        duration.setLabel("Durata");
        duration.setValue(movie.getDuration());
        dialogLayout.add(duration);

        TextField classification = new TextField();
        classification.setLabel("Clasificare");
        classification.setValue(movie.getClassification());
        dialogLayout.add(classification);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        MovieDAO movieDAO = context.getBean(MovieDAO.class);
        context.close();

        Button signInButton = new Button("Modifica", e -> movieDAO.updateMovie(new Movie(title.getValue(), poster.getValue(), Float.parseFloat(rating.getValue()),
                description.getValue(), cast.getValue(), director.getValue(), writer.getValue(), genre.getValue(),
                trailer.getValue(), duration.getValue(), classification.getValue()), movieDAO.getMovieIdByTitle(movie.getTitle())));
        signInButton.getElement().getStyle().set("background","#f9003a");
        signInButton.getElement().getStyle().set("color","#ffffff");
        signInButton.getElement().getStyle().set("font-size","15px");
        signInButton.getElement().getStyle().set("font-family","Rockwell");

        VerticalLayout verticalLayoutButton = new VerticalLayout();
        verticalLayoutButton.add(signInButton);
        verticalLayoutButton.setAlignItems(Alignment.CENTER);

        dialogLayout.add(verticalLayoutButton);
        dialogLayout.setAlignItems(Alignment.STRETCH);

        return dialogLayout;
    }

    private static VerticalLayout createDialogModifyRepresentation(Dialog dialog) {

        Label pageTitle = new Label("Modifica rulare");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","30px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle.getElement().getStyle().set("text-align", "center");

        VerticalLayout buttons = new VerticalLayout();
        buttons.setAlignItems(Alignment.CENTER);

        AnnotationConfigApplicationContext contextMovies = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        RepresentationDAO representationDAO = contextMovies.getBean(RepresentationDAO.class);
        MovieDAO movieDAO = contextMovies.getBean(MovieDAO.class);
        ScreenDAO screenDAO = contextMovies.getBean(ScreenDAO.class);
        TheatreDAO theatreDAO = contextMovies.getBean(TheatreDAO.class);
        List<Representation> representationList = representationDAO.getAllRepresentations();
        contextMovies.close();

        Grid<Representation> grid = new Grid<>(Representation.class, false);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(r -> r.getMovieTitle(movieDAO, r.getMovie())).setHeader("Movie");
        grid.addColumn(r -> toDayOfWeekInt(r.getDayOfWeek())).setHeader("Day of week");
        grid.addColumn(Representation::getStartTime).setHeader("Start time");
        grid.addColumn(r -> r.getScreenName(screenDAO, r.getScreen())).setHeader("Screen");
        grid.addColumn(r -> r.getCinemaLocation(theatreDAO, screenDAO, r.getScreen())).setHeader("Cinema");
        grid.addColumn(Representation::getTypeOfPlay).setHeader("Type");
        grid.setItems(representationList);

        Button ok = new Button("Modifica", e -> {
            for(Representation x : grid.getSelectedItems()){
                Dialog dialogModifyRepresentationData = new Dialog();
                VerticalLayout dialogModifyMovieDataV = createDialogLayoutModifyRepresentationData(dialogModifyRepresentationData, x);
                dialogModifyRepresentationData.add(dialogModifyMovieDataV);
                dialogModifyRepresentationData.open();
            }
        });
        ok.getElement().getStyle().set("background","#f9003a");
        ok.getElement().getStyle().set("color","#ffffff");
        ok.getElement().getStyle().set("font-size","20px");
        ok.getElement().getStyle().set("font-family","Rockwell");
        buttons.add(ok);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(pageTitle);
        dialogLayout.add(grid);
        dialogLayout.add(buttons);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "1200px").set("height", "800px").set("max-width", "100%");
        return dialogLayout;
    }

    private static VerticalLayout createDialogLayoutModifyRepresentationData(Dialog dialogAddRepresentation, Representation representation) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        HorizontalLayout topWholeLayout = new HorizontalLayout();
        Image image = new Image("https://wallpaperboat.com/wp-content/uploads/2019/11/cinema-01.jpg", "sorry :(");
        image.setHeight(200, Unit.PIXELS);
        topWholeLayout.add(image);


        VerticalLayout topLayout = new VerticalLayout();

        Label pageTitle = new Label("FOCUS");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        topLayout.add(pageTitle);

        Label pageTitle1 = new Label("CINEMA");
        pageTitle1.getElement().getStyle().set("fontWeight","bold");
        pageTitle1.getElement().getStyle().set("font-size","50px");
        pageTitle1.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle1.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle1.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle1.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle1.getElement().getStyle().set("margin-left", "50px");
        topLayout.add(pageTitle1);

        topLayout.setPadding(false);
        topLayout.setMargin(false);
        topWholeLayout.setPadding(false);
        topWholeLayout.add(topLayout);
        dialogLayout.add(topWholeLayout);

        Label registerLabel = new Label("Modifica rulare");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        dialogLayout.add(registerLabel);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        RepresentationDAO representationDAO = context.getBean(RepresentationDAO.class);
        MovieDAO movieDAO = context.getBean(MovieDAO.class);
        ScreenDAO screenDAO = context.getBean(ScreenDAO.class);
        TheatreDAO theatreDAO = context.getBean(TheatreDAO.class);
        context.close();

        TextField title = new TextField();
        title.setLabel("Titlul filmului");
        title.setValue(representation.getMovieTitle(movieDAO, representation.getMovie()));
        dialogLayout.add(title);

        TextField dayOfWeek = new TextField();
        dayOfWeek.setLabel("Ziua saptamanii");
        dayOfWeek.setValue(toDayOfWeekInt(representation.getDayOfWeek()));
        dialogLayout.add(dayOfWeek);

        TextField startTime = new TextField();
        startTime.setLabel("Ora de incepere");
        startTime.setValue(representation.getStartTime());
        dialogLayout.add(startTime);

        TextField screen = new TextField();
        screen.setLabel("Sala");
        screen.setValue(representation.getScreenName(screenDAO, representation.getScreen()));
        dialogLayout.add(screen);

        TextField cinema = new TextField();
        cinema.setLabel("Cinema");
        cinema.setValue(representation.getCinemaLocation(theatreDAO, screenDAO, representation.getScreen()));
        dialogLayout.add(cinema);

        TextField typeOfPlay = new TextField();
        typeOfPlay.setLabel("Tipul rularii");
        typeOfPlay.setValue(representation.getTypeOfPlay());
        dialogLayout.add(typeOfPlay);

        Button signInButton = new Button("Modifica", e -> representationDAO.updateRepresentation(new Representation(movieDAO.getMovieIdByTitle(title.getValue()),
                toIntDayOfWeek(dayOfWeek.getValue()), startTime.getValue(), screenDAO.getIdOfScreen(cinema.getValue(), screen.getValue()), typeOfPlay.getValue()),
                representationDAO.getId(representation)));
        signInButton.getElement().getStyle().set("background","#f9003a");
        signInButton.getElement().getStyle().set("color","#ffffff");
        signInButton.getElement().getStyle().set("font-size","15px");
        signInButton.getElement().getStyle().set("font-family","Rockwell");

        dialogLayout.add(signInButton);
        dialogLayout.setAlignItems(Alignment.CENTER);

        return dialogLayout;
    }

    private HorizontalLayout createDialogLayoutMovieProgramme(Dialog dialogMovieProgramme, Movie movie) {
        HorizontalLayout dialogLayout = new HorizontalLayout();
        dialogLayout.setPadding(false);

        VerticalLayout leftLayout = new VerticalLayout();
        Image image = new Image(movie.getPoster(), "sorry :(");
        image.setHeight(470, Unit.PIXELS);
        leftLayout.add(image);

        Label pageTitle = new Label("FOCUS CINEMA");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        leftLayout.add(pageTitle);

        VerticalLayout rightLayout = new VerticalLayout();

        Label connect = new Label(movie.getTitle());
        connect.getElement().getStyle().set("font-size","50px");
        connect.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        connect.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        connect.getElement().getStyle().set("-webkit-background-clip","text");
        connect.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        rightLayout.add(connect);

        AnnotationConfigApplicationContext contextRepresentation = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        RepresentationDAO representationDAO = contextRepresentation.getBean(RepresentationDAO.class);
        MovieDAO movieDAO = contextRepresentation.getBean(MovieDAO.class);
        TheatreDAO theatreDAO = contextRepresentation.getBean(TheatreDAO.class);
        contextRepresentation.close();

        List<String> weekDays = new ArrayList<>();
        weekDays.add("Astazi");
        weekDays.add("Vi");
        weekDays.add("Sa");
        weekDays.add("Du");
        weekDays.add("Lu");
        weekDays.add("Ma");
        weekDays.add("Mi");

        List<Theatre> theatreList = theatreDAO.getAllTheatres();

        for(int j = 0; j < theatreList.size(); j ++){
            List<Representation> representationList = representationDAO.getAllRepresentationsOfMovieInTheatre(movieDAO.getMovieIdByTitle(movie.getTitle()), theatreDAO.getId(theatreList.get(j)));

            Label cinema = new Label(theatreList.get(j).getLocation());
            cinema.getElement().getStyle().set("font-size","25px");
            cinema.getElement().getStyle().set("font-family","Rockwell");
            //pentru gradient
            cinema.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
            cinema.getElement().getStyle().set("-webkit-background-clip","text");
            cinema.getElement().getStyle().set("-webkit-text-fill-color","transparent");
            rightLayout.add(cinema);

            for(int i = 0; i < weekDays.size(); i++){
                Label weekDay = new Label(weekDays.get(i));
                weekDay.getElement().getStyle().set("font-size","20px");
                weekDay.getElement().getStyle().set("font-family","Rockwell");
                weekDay.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
                weekDay.getElement().getStyle().set("-webkit-background-clip","text");
                weekDay.getElement().getStyle().set("-webkit-text-fill-color","transparent");
                rightLayout.add(weekDay);

                HorizontalLayout movieStartHours = new HorizontalLayout();

                for(Representation r : representationList)
                    if(r.getDayOfWeek().equals(i + 1)){
                        Button representation = new Button(r.getStartTime(), e -> {
                            Dialog dialogReservation = new Dialog();

                            VerticalLayout dialogReservationV = createDialogLayoutBooking(dialogReservation, r, this.customer);
                            dialogReservation.add(dialogReservationV);
                            dialogReservation.open();
                        });
                        representation.getElement().getStyle().set("background","#f9003a");
                        representation.getElement().getStyle().set("color","#ffffff");
                        representation.getElement().getStyle().set("font-size","15px");
                        representation.getElement().getStyle().set("font-family","Rockwell");
                        movieStartHours.add(representation);
                    }

                rightLayout.add(movieStartHours);
                Label movieLine = new Label("_________________________");
                movieLine.getElement().getStyle().set("font-size","20px");
                movieLine.getElement().getStyle().set("font-family","Rockwell");
                movieLine.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
                movieLine.getElement().getStyle().set("-webkit-background-clip","text");
                movieLine.getElement().getStyle().set("-webkit-text-fill-color","transparent");
                rightLayout.add(movieLine);
            }
        }

        dialogLayout.add(leftLayout);
        dialogLayout.add(rightLayout);

        return dialogLayout;
    }

    private static VerticalLayout createDialogModifyPrice(Dialog dialog) {

        Label pageTitle = new Label("Modifica pret bilete");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","30px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle.getElement().getStyle().set("text-align", "center");

        VerticalLayout buttons = new VerticalLayout();
        buttons.setAlignItems(Alignment.CENTER);

        AnnotationConfigApplicationContext contextMovies = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        PriceDAO priceDAO = contextMovies.getBean(PriceDAO.class);
        List<Price> priceList = priceDAO.getAllPrices();
        contextMovies.close();

        Grid<Price> grid = new Grid<>(Price.class, false);
        grid.setSelectionMode(Grid.SelectionMode.SINGLE);
        grid.addColumn(Price::getTypeOfPlay).setHeader("Tipul rularii");
        grid.addColumn(Price::getAmount).setHeader("Pretul biletului");
        grid.setItems(priceList);

        Button ok = new Button("Modifica", e -> {
            for(Price x : grid.getSelectedItems()){
                Dialog dialogModifyPriceData = new Dialog();
                VerticalLayout dialogModifyMovieDataV = createDialogLayoutModifyPriceData(dialogModifyPriceData, x);
                dialogModifyPriceData.add(dialogModifyMovieDataV);
                dialogModifyPriceData.open();
            }
        });
        ok.getElement().getStyle().set("background","#f9003a");
        ok.getElement().getStyle().set("color","#ffffff");
        ok.getElement().getStyle().set("font-size","20px");
        ok.getElement().getStyle().set("font-family","Rockwell");
        buttons.add(ok);

        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.add(pageTitle);
        dialogLayout.add(grid);
        dialogLayout.add(buttons);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "1200px").set("height", "800px").set("max-width", "100%");
        return dialogLayout;
    }

    private static VerticalLayout createDialogLayoutModifyPriceData(Dialog dialogModifyPriceData, Price price) {
        VerticalLayout dialogLayout = new VerticalLayout();
        dialogLayout.setPadding(false);

        HorizontalLayout topWholeLayout = new HorizontalLayout();
        Image image = new Image("https://c4.wallpaperflare.com/wallpaper/22/762/507/film-movie-filmmaker-movie-director-wallpaper-preview.jpg", "sorry :(");
        image.setHeight(200, Unit.PIXELS);
        topWholeLayout.add(image);


        VerticalLayout topLayout = new VerticalLayout();

        Label pageTitle = new Label("FOCUS");
        pageTitle.getElement().getStyle().set("fontWeight","bold");
        pageTitle.getElement().getStyle().set("font-size","50px");
        pageTitle.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        topLayout.add(pageTitle);

        Label pageTitle1 = new Label("CINEMA");
        pageTitle1.getElement().getStyle().set("fontWeight","bold");
        pageTitle1.getElement().getStyle().set("font-size","50px");
        pageTitle1.getElement().getStyle().set("font-family","Rockwell");
        //pentru gradient
        pageTitle1.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        pageTitle1.getElement().getStyle().set("-webkit-background-clip","text");
        pageTitle1.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        pageTitle1.getElement().getStyle().set("margin-left", "50px");
        topLayout.add(pageTitle1);

        topLayout.setPadding(false);
        topLayout.setMargin(false);
        topWholeLayout.setPadding(false);
        topWholeLayout.add(topLayout);
        dialogLayout.add(topWholeLayout);

        Label registerLabel = new Label("Modifica pret bilete");
        registerLabel.getElement().getStyle().set("font-size","30px");
        //pentru gradient
        registerLabel.getElement().getStyle().set("background-image","linear-gradient(45deg, #f9003a, #53cad7)");
        registerLabel.getElement().getStyle().set("-webkit-background-clip","text");
        registerLabel.getElement().getStyle().set("-webkit-text-fill-color","transparent");
        dialogLayout.add(registerLabel);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DatabaseConfig.class);
        PriceDAO priceDAO= context.getBean(PriceDAO.class);
        context.close();

        TextField typeOfPlayTextField = new TextField();
        typeOfPlayTextField.setLabel("Tipul rularii");
        typeOfPlayTextField.setValue(price.getTypeOfPlay());
        typeOfPlayTextField.setEnabled(false);
        dialogLayout.add(typeOfPlayTextField);

        TextField priceTextField = new TextField();
        priceTextField.setLabel("Pret bilet");
        priceTextField.setValue(Float.toString(price.getAmount()));
        dialogLayout.add(priceTextField);

        Button signInButton = new Button("Modifica", e -> {
            priceDAO.updatePrice(Float.parseFloat(priceTextField.getValue()), price.getTypeOfPlay());
        });
        signInButton.getElement().getStyle().set("background","#f9003a");
        signInButton.getElement().getStyle().set("color","#ffffff");
        signInButton.getElement().getStyle().set("font-size","15px");
        signInButton.getElement().getStyle().set("font-family","Rockwell");

        dialogLayout.add(signInButton);
        dialogLayout.setAlignItems(Alignment.CENTER);

        return dialogLayout;
    }

    private static int toIntDayOfWeek(String dayOfWeek){
        if(dayOfWeek.equals("Jo"))
            return 1;
        if(dayOfWeek.equals("Vi"))
            return 2;
        if(dayOfWeek.equals("Sa"))
            return 3;
        if(dayOfWeek.equals("Du"))
            return 4;
        if(dayOfWeek.equals("Lu"))
            return 5;
        if(dayOfWeek.equals("Ma"))
            return 6;
        if(dayOfWeek.equals("Mi"))
            return 7;
        return  0;
    }

    private static String toDayOfWeekInt(int dayOfWeek){
        switch (dayOfWeek){
            case 1: return "Jo";
            case 2: return "Vi";
            case 3: return "Sa";
            case 4: return "Du";
            case 5: return "Lu";
            case 6: return "Ma";
            case 7: return "Mi";
        }
        return "";
    }
}