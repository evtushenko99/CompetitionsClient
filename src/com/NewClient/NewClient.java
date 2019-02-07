package com.NewClient;

import com.Connection.Commands;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.model.Athlet;
import com.model.Competition;
import com.model.Seat;
import com.model.Team;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class NewClient implements Runnable {
    private static final Gson GSON;

    static{
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        GSON = builder.create();
    }

    private final String ip;
    private final int port;
    private final Scanner scanner;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private int teamsId = 5;

    NewClient(String ip, int port){
        scanner = new Scanner(System.in);
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run(){
        char answer = 'y';
        while (Character.toLowerCase(answer) != 'n') {

            try {
                startConnection();
                sendCommand();
            } catch (IOException e) {
                e.printStackTrace();
                break;
            } finally {
                stopConnection();
            }

            System.out.print("Would you like to send one more message (y/n)? ");
            answer = scanner.nextLine().charAt(0);
        }
        System.out.println("Client shuts down");
    }

    private void startConnection() throws IOException{
        socket = new Socket(ip, port);
        outputStream = new DataOutputStream(socket.getOutputStream());
        inputStream = new DataInputStream(socket.getInputStream());
    }

    private void sendCommand() throws IOException{
        System.out.print("Type command: ");
        Commands command = Commands.valueOf(scanner.nextLine());
        String argument;

        switch (command) {
            case GET_TEAMS:
                sendGetTeams();
                break;
            case GET_ONE_TEAM:
                System.out.print("Type team_id: ");
                argument = scanner.nextLine();
                sendGetTeamById(Integer.valueOf(argument));
                break;
            case ADD_TEAM:
                Team teamToAdd = readTeam();
                sendAddTeam(teamToAdd);
                break;
            case UPDATE_TEAM:
                System.out.print("Type name_of_team: ");
                argument = scanner.nextLine();
                Team teamForUpdate = readTeamForUpdate();
                sendTeamForUpdate(argument, teamForUpdate);
                break;
            case DELETE_TEAM:
                System.out.print("Type team_id: ");
                argument = scanner.nextLine();
                sendTeamForDelete(argument);
                break;

            case GET_ATHLETES:
                sendGetAthletes();
                break;
            case GET_ONE_ATHLET:
                System.out.print("Type athlet_id: ");
                argument = scanner.nextLine();
                sendGetAthletById(Integer.valueOf(argument));
                break;
            case ADD_ATHLET:
                Athlet athletToAdd = readAthlet();
                sendAddAthlet(athletToAdd);
                break;
            case UPDATE_ATHLET:
                System.out.print("Type athlet_last_name: ");
                argument = scanner.nextLine();
                Athlet athletForUpdate = readAthletForUpdate();
                sendAthletForUpdate(argument, athletForUpdate);
                break;
            case DELETE_ATHLET:
                System.out.print("Type athlet_id: ");
                argument = scanner.nextLine();
                sendAthletForDelete(argument);
                break;

            case GET_COMPETITIONS:
                sendGetCompetitions();
                break;
            case GET_ONE_COMPETITION:
                System.out.print("Type competition_id: ");
                argument = scanner.nextLine();
                sendGetCompetitionById(Integer.valueOf(argument));
                break;
            case ADD_COMPETITION:
                Competition competitionToAdd = readCompetition();
                sendAddCompetition(competitionToAdd);
                break;
            case UPDATE_COMPETITION:
                System.out.print("Type competition_id: ");
                argument = scanner.nextLine();
                Competition competitionForUpdate = readCompetitionForUpdate();
                sendCompetitionForUpdate(Integer.valueOf(argument), competitionForUpdate);
                break;
            case DELETE_COMPETITION:
                System.out.print("Type id: ");
                argument = scanner.nextLine();
                sendCompetitionForDelete(argument);
                break;

            case GET_SEATS:
                sendGetSeats();
                break;
            case GET_ONE_SEAT:
                System.out.print("Type seat_id: ");
                argument = scanner.nextLine();
                sendGetSeatById(Integer.valueOf(argument));
                break;
        }
    }


    private void sendGetSeats() throws IOException{
        outputStream.writeUTF(Commands.GET_SEATS.toString());
        handleGetSeats();
    }

    private void handleGetSeats() throws IOException{
        List<Seat> seats = Arrays.asList(GSON.fromJson(inputStream.readUTF(), Seat[].class));
        System.out.println(seats.toString());
    }

    private void sendGetSeatById(Integer id) throws IOException{
        outputStream.writeUTF(Commands.GET_ONE_SEAT.toString() + "'" + id);
        handleGetSeatById();
    }

    private void handleGetSeatById() throws IOException{
        Seat seat = GSON.fromJson(inputStream.readUTF(), Seat.class);
        System.out.println(seat);
    }

    private Competition readCompetition(){
        System.out.print("Type `tournament_name`:");
        String tournament_name = scanner.nextLine();
        System.out.print("Type locations`:");
        String locations = scanner.nextLine();
        System.out.print("Type kind_of_sport`:");
        String kind_of_sport = scanner.nextLine();
        System.out.print("Type time_of_comp`:");
        String timeOfComp = scanner.nextLine();
        System.out.print("Type results`:");
        String results = scanner.nextLine();
        return new Competition(-1, tournament_name, locations, kind_of_sport, timeOfComp, results);
    }

    private Competition readCompetitionForUpdate(){
        System.out.print("Type new `tournament_name`:");
        String tournament_name = scanner.nextLine();
        System.out.print("Type new locations`:");
        String locations = scanner.nextLine();
        System.out.print("Type new kind_of_sport`:");
        String kind_of_sport = scanner.nextLine();
        System.out.print("Type new time_of_comp`:");
        String timeOfComp = scanner.nextLine();
        System.out.print("Type new results`:");
        String results = scanner.nextLine();
        return new Competition(-1, tournament_name, locations, kind_of_sport, timeOfComp, results);
    }

    private void sendGetCompetitions() throws IOException{
        outputStream.writeUTF(Commands.GET_COMPETITIONS.toString());
        handleGetCompetitions();
    }

    private void handleGetCompetitions() throws IOException{
        List<Competition> competitions = Arrays.asList(GSON.fromJson(inputStream.readUTF(), Competition[].class));
        System.out.println(competitions.toString());
    }

    private void sendGetCompetitionById(Integer id) throws IOException{
        outputStream.writeUTF(Commands.GET_ONE_COMPETITION.toString() + "'" + id);
        handleGetCompetitionById();
    }

    private void handleGetCompetitionById() throws IOException{
        Competition competition = GSON.fromJson(inputStream.readUTF(), Competition.class);
        System.out.println(competition);
    }

    private void sendAddCompetition(Competition competition) throws IOException{
        outputStream.writeUTF(Commands.ADD_COMPETITION.toString() + "'" + GSON.toJson(competition));
        System.out.println(inputStream.readUTF());
    }

    private void sendCompetitionForUpdate(int id, Competition competitionForUpdate) throws IOException{
        outputStream.writeUTF(Commands.UPDATE_COMPETITION.toString() + "'" + id + "'" + GSON.toJson(competitionForUpdate));

    }

    private void sendCompetitionForDelete(String argument) throws IOException{
        outputStream.writeUTF(Commands.DELETE_COMPETITION.toString() + "'" + argument);
        System.out.println(inputStream.readUTF());
    }

    private Athlet readAthlet(){
        System.out.print("Type `athlet_first_name`:");
        String athletFirstName = scanner.nextLine();
        System.out.print("Type athlet_last_name`:");
        String athletLastName = scanner.nextLine();
        System.out.print("Type athlet_age`:");
        String athletAge = scanner.nextLine();
        return new Athlet(athletFirstName, athletLastName, athletAge);
    }

    private Athlet readAthletForUpdate(){
        System.out.print("Type new `athlet_first_name`:");
        String athletFirstName = scanner.nextLine();
        System.out.print("Type new athlet_last_name`:");
        String athletLastName = scanner.nextLine();
        System.out.print("Type new athlet_age`:");
        String athletAge = scanner.nextLine();
        return new Athlet(athletFirstName, athletLastName, athletAge);
    }

    private void sendGetAthletes() throws IOException{
        outputStream.writeUTF(Commands.GET_ATHLETES.toString());
        handleGetAthletes();
    }

    private void handleGetAthletes() throws IOException{
        List<Athlet> athletes = Arrays.asList(GSON.fromJson(inputStream.readUTF(), Athlet[].class));
        System.out.println(athletes.toString());
    }

    private void sendGetAthletById(Integer id) throws IOException{
        outputStream.writeUTF(Commands.GET_ONE_ATHLET.toString() + "'" + id);
        handleGetAthletById();
    }

    private void handleGetAthletById() throws IOException{
        Athlet athlet = GSON.fromJson(inputStream.readUTF(), Athlet.class);
        System.out.println(athlet);
    }

    private void sendAddAthlet(Athlet athletToAdd) throws IOException{
        outputStream.writeUTF(Commands.ADD_ATHLET.toString() + "'" + GSON.toJson(athletToAdd));
        System.out.println(inputStream.readUTF());
    }

    private void sendAthletForUpdate(String athletesLastName, Athlet athletForUpdate) throws IOException{
        outputStream.writeUTF(Commands.UPDATE_ATHLET.toString() + "'" + athletesLastName + "'" + GSON.toJson(athletForUpdate));
    }

    private void sendAthletForDelete(String argument) throws IOException{
        outputStream.writeUTF(Commands.DELETE_ATHLET.toString() + "'" + argument);
        System.out.println(inputStream.readUTF());
    }

    private Team readTeam(){
        teamsId = teamsId + 1;
        System.out.print("Type `name_of_team`:");
        String nameOfTeam = scanner.nextLine();
        System.out.print("Type  `coach`:");
        String coach = scanner.nextLine();
        return new Team(teamsId, nameOfTeam, coach, "1");
    }

    private Team readTeamForUpdate(){
        teamsId = teamsId + 1;
        System.out.print("Type new `name_of_team`:");
        String nameOfTeam = scanner.nextLine();
        System.out.print("Type new `coach`:");
        String coach = scanner.nextLine();
        return new Team(teamsId, nameOfTeam, coach, "1");
    }

    private void sendGetTeams() throws IOException{
        outputStream.writeUTF(Commands.GET_TEAMS.toString());
        handleGetTeams();
    }

    private void handleGetTeams() throws IOException{
        List<Team> teams = Arrays.asList(GSON.fromJson(inputStream.readUTF(), Team[].class));
        System.out.println(teams.toString());
    }

    private void sendGetTeamById(int id) throws IOException{
        outputStream.writeUTF(Commands.GET_ONE_TEAM.toString() + "'" + id);
        handleGetTeamById();
    }

    private void handleGetTeamById() throws IOException{
        Team team = GSON.fromJson(inputStream.readUTF(), Team.class);
        System.out.println(team);
    }

    private void sendAddTeam(Team team) throws IOException{
        outputStream.writeUTF(Commands.ADD_TEAM.toString() + "'" + GSON.toJson(team));
        System.out.println(inputStream.readUTF());
    }

    private void sendTeamForUpdate(String argument, Team teamForUpdate) throws IOException{
        outputStream.writeUTF(Commands.UPDATE_TEAM.toString() + "'" + argument + "'" + GSON.toJson(teamForUpdate));
    }

    private void sendTeamForDelete(String argument) throws IOException{
        outputStream.writeUTF(Commands.DELETE_TEAM.toString() + "'" + argument);
        System.out.println(inputStream.readUTF());
    }


    private void stopConnection(){
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (Exception ignored) {

        }
    }
}


