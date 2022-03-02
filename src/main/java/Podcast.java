import java.io.File;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
public class Podcast {
    Long currentFrame;
    Clip clip;
    String status;
    AudioInputStream inputStream;
    String podId;
    String artist;
    String genre;
    String dateOfPodcast;
    public String getPodId() {
        return podId;
    }
    public void setPodId(String podId) {
        this.podId = podId;
    }
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }
    public String getDateOfPodcast() {
        return dateOfPodcast;
    }
    public void setDateOfPodcast(String dateOfPodcast) {
        this.dateOfPodcast = dateOfPodcast;
    }
    public Podcast() {

    }
    public Podcast(String podId, String artist, String genre, String dateOfPodcast) {
        super();
        this.podId = podId;
        this.artist = artist;
        this.genre = genre;
        this.dateOfPodcast = dateOfPodcast;
    }

    public static List<Podcast> storePodcastInArray(Connection conn) throws SQLException{
        List<Podcast> podcastList=new ArrayList<Podcast>();

        Statement statementObj=conn.createStatement();
        ResultSet rs=statementObj.executeQuery("select * from Podcast");

        while(rs.next()) {
            podcastList.add(new Podcast(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4)));
        }


        return podcastList;
    }


    public void displayPodcast(Connection conn) throws SQLException {
        try {
            List<Podcast> podcastList=new ArrayList<Podcast>();
            podcastList=storePodcastInArray(conn);
            System.out.println("\n______________________________________");
            System.out.printf("\n%-10s %-20s %-20s %-20s","Podcast ID","Artist","Genre","Date");
            System.out.println("\n______________________________________");
            podcastList.forEach(p->{System.out.printf("%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getArtist(),p.getGenre(),p.getDateOfPodcast());});
            Playpod();

        } catch (SQLException e) {
            System.out.print("\nOpps try Again");
        }

    }
    public void Playpod() {
        try {
            Scanner sc=new Scanner(System.in);
            System.out.println("\n\nEnter Podcast ID:");
            String podID=sc.next();
            System.out.print("\tPlaying.....");
            String url="C:\\Users\\Dell\\Downloads\\"+podID+".wav";
            clip=AudioSystem.getClip();
            File file=new File(url);

            inputStream=AudioSystem.getAudioInputStream(file.getAbsoluteFile());

            clip.open(inputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            status = "play";

            while(true) {
                System.out.print("\n\n\t1. Pause\t2. Resume\t3. Restart\t4. Stop");

                int choice=sc.nextInt();
                operations(choice, url);
                if(choice==4) {
                    System.out.print("\n\tReally wanna stop playing?(Y/N) ");
                    String plays=sc.next();
                    if(plays.equalsIgnoreCase("Y"))
                    {
                        JukeBox menu=new JukeBox();
                        menu.songMenu();
                    }
                    else if(plays.equalsIgnoreCase("N")) {
                        break;
                    }
                    else {
                        System.out.print("\nEnter 3");
                        continue;
                    }
                }
            }
        }
        catch(Exception e) {
            System.out.print("\n\nPodcast not available.We will upload it Soon!");
        }
    }
    public void operations(int choice, String url) {
        try {
            switch(choice) {
                case 1:
                    this.currentFrame=this.clip.getMicrosecondPosition();
                    clip.stop();
                    status="paused";
                    break;
                case 2:

                    clip.setMicrosecondPosition(currentFrame);
                    clip.start();
                    status="play";
                    break;

                case 3:
                    clip.stop();
                    clip.close();

                    clip=AudioSystem.getClip();
                    File file=new File(url);

                    inputStream=AudioSystem.getAudioInputStream(file.getAbsoluteFile());

                    clip.open(inputStream);
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                    clip.start();
                    status = "play";
                    break;
                case 4:
                    currentFrame=0L;
                    clip.stop();
                    clip.close();
                    break;

            }
        }

        catch(Exception e) {
            e.getMessage();
        }
    }
    public void searchPodcast(Connection conn) throws SQLException  {
        Scanner scanObj=new Scanner(System.in);
        List<Podcast> podcastList=new ArrayList<Podcast>();
        podcastList=storePodcastInArray(conn);
        Song song=new Song();
        try {
            System.out.println("\nEnter your choice to search from Podcast:\t1. Artist\t2. Genre\t3. Date of Podcast");
            int choice=scanObj.nextInt();
            switch(choice) {
                case 1:
                    System.out.print("\n\nEnter Artist: ");
                    String Artist=scanObj.next();
                    System.out.println("______________________");
                    System.out.printf("%-10s %-21s %-14s %-15s\n","PodId","Artist","Genre","DateOfPodcast");
                    System.out.println("______________________");
                    int roweff=searchByArtist(podcastList,Artist);
                    Playpod();
                    break;
                case 2:
                    System.out.print("\nEnter Genre: ");
                    String genre=scanObj.next();
                    System.out.println("______________________");
                    System.out.printf("%-10s %-21s %-14s %-15s\n","PodId","Artist","Genre","DateOfPodcast");
                    System.out.println("______________________");
                    int row=searchByGenre(podcastList,genre);

                    Playpod();
                    break;
                case 3:
                    System.out.print("\nEnter Date:(YYYY-MM-DD) ");
                    String date=scanObj.next();
                    System.out.println("______________________");
                    System.out.printf("%-10s %-21s %-14s %-15s\n","PodId","Artist","Genre","DateOfPodcast");
                    System.out.println("______________________");
                    int row1=searchByDate(podcastList,date);


                    Playpod();
                    break;
                default:
                    System.out.print("\nInvalid choice");
            }
        } catch (Exception e) {
            System.out.print("\n\nUnable to Search now Try later :)");
        }
    }
    public static int searchByArtist(List<Podcast> podList, String Artist) {
        List<Podcast> list=new ArrayList<Podcast>();
        podList.stream().filter(s->s.getArtist().contains(Artist))
                .collect(Collectors.toList())
                .forEach(p->{list.add(p);
                    System.out.printf("%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getArtist(),p.getGenre(),p.getDateOfPodcast());
                });
        return list.size();
    }
    public static int searchByGenre(List<Podcast> podList, String Genre) {
        List<Podcast> list=new ArrayList<Podcast>();
        podList.stream().filter(s->s.getGenre().contains(Genre))
                .collect(Collectors.toList())
                .forEach(p->{list.add(p);
                    System.out.printf("%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getArtist(),p.getGenre(),p.getDateOfPodcast());
                });
        return list.size();
    }
    public static int searchByDate(List<Podcast> podList, String Date) {
        List<Podcast> list=new ArrayList<Podcast>();
        podList.stream().filter(s->s.getDateOfPodcast().contains(Date))
                .collect(Collectors.toList())
                .forEach(p->{list.add(p);
                    System.out.printf("%-10s %-20s %-15s %-15s\n",p.getPodId(),p.getArtist(),p.getGenre(),p.getDateOfPodcast());
                });
        return list.size();
    }
}