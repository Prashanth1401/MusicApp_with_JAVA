import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Song {
    Long currentFrame;
    Clip clip;
    String status;
    AudioInputStream inputStream;
    String songID;
    String songName;
    String songArtist;
    String songGenre;
    String songAlbum;
    String songDuration;

    public Song(String songID, String songName, String songArtist, String songGenre, String songAlbum,
                String songDuration) {
        super();
        this.songID = songID;
        this.songName = songName;
        this.songArtist = songArtist;
        this.songGenre = songGenre;
        this.songAlbum = songAlbum;
        this.songDuration = songDuration;
    }

    public Song() {

    }

    public String getSongID() {
        return songID;
    }

    public void setSongID(String songID) {
        this.songID = songID;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        this.songArtist = songArtist;
    }

    public String getSongGenre() {
        return songGenre;
    }

    public void setSongGenre(String songGenre) {
        this.songGenre = songGenre;
    }

    public String getSongAlbum() {
        return songAlbum;
    }

    public void setSongAlbum(String songAlbum) {
        this.songAlbum = songAlbum;
    }

    public String getSongDuration() {
        return songDuration;
    }

    public void setSongDuration(String songDuration) {
        this.songDuration = songDuration;
    }


    public List<Song> storeSongsInArrayList(Connection conn) throws SQLException {
        Statement statementObj=conn.createStatement();
        ResultSet rs=statementObj.executeQuery("select * from Song");

        ArrayList<Song> songList=new ArrayList<Song>();
        while(rs.next()) {
            songList.add(new Song(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6)));
        }

        return songList;
    }


    public void displaySongs(Connection conn) throws SQLException {
        //Creating a new statement obj of class Statement and assigning it to connection statement//function call
        Statement statement=conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
        ResultSet rs=statement.executeQuery("select * from Song");
        System.out.println("\n_______________________________________");
        System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
        System.out.println("\n_______________________________________");
        while(rs.next()) {
            System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6));
        }
        playSongs();

    }

    public void playSongs() {
        try {
            Scanner sc=new Scanner(System.in);
            System.out.println("\n\nEnter song ID:");
            String SongID=sc.next();
            System.out.print("\tPlaying....");
            String url="C:\\Users\\Prashanth patel\\IdeaProjects\\JukBox2\\src\\main\\resources\\"+SongID+".wav";
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
            System.out.print("\n\nSong not available.We will upload it Soon!");
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

    public void searchSongs(List<Song> songList) {
        try {
            Scanner scanObj=new Scanner(System.in);
            System.out.println("\nSearch by:\t1.Song\t2.Artist\t3.Genre\t4.Album");
            int choice=scanObj.nextInt();
            switch(choice) {
                case 1:
                    System.out.print("\nEnter Song to Search: ");
                    String songname=scanObj.next();
                    System.out.println("\n_______________________________________");
                    System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
                    System.out.println("\n_______________________________________");
                    int roweff=searchByName(songList,songname);
                    playSongs();
                    break;
                case 2:
                    System.out.print("\nEnter Artist to Search: ");
                    String artistname=scanObj.next();
                    System.out.println("\n_______________________________________");
                    System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
                    System.out.println("\n_______________________________________");
                    int roweff1=searchByArtist(songList,artistname);
                    playSongs();
                    break;
                case 3:
                    System.out.print("\nEnter Genre to Search: ");
                    String genreName=scanObj.next();
                    System.out.println("\n_______________________________________");
                    System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
                    System.out.println("\n_______________________________________");
                    int roweff2=searchByGenre(songList,genreName);
                    playSongs();
                    break;
                case 4:
                    System.out.print("\nEnter Album to Search: ");
                    String albumName=scanObj.next();
                    System.out.println("\n_______________________________________");
                    System.out.printf("\n%-10s %-30s %-20s %-15s %-20s %-15s","SongID","Song Name","Artist","Genre","Album","Duration");
                    System.out.println("\n_______________________________________");
                    int roweff3=searchByAlbum(songList,albumName);
                    playSongs();
                    break;
                default:
                    System.out.print("\nInvalid choice");
            }
        }
        catch(Exception e) {
            System.out.print("\n\nUnable Searching at this moment");
        }
    }
    public static int searchByName(List<Song> songList,String Songname)
    {
        List<Song> list=new ArrayList<Song>();
        songList.stream().filter(p->p.getSongName().contains(Songname))
                .collect(Collectors.toList())
                .forEach(s->{list.add(s);System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());});
        return list.size();
    }
    public static int searchByArtist(List<Song> songList, String artistName) {
        List<Song> list=new ArrayList<Song>();
        songList.stream().filter(p->p.getSongArtist().contains(artistName))
                .collect(Collectors.toList())
                .forEach(s->{list.add(s);System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());});

        return list.size();
    }
    public static int searchByGenre(List<Song> songList, String genreName) {
        List<Song> list=new ArrayList<Song>();
        songList.stream()
                .filter(p->p.getSongGenre().contains(genreName))
                .collect(Collectors.toList())
                .forEach(s->{list.add(s);System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());});
        return list.size();

    }
    public static int searchByAlbum(List<Song> songList, String albumName) {
        List<Song> list=new ArrayList<Song>();
        songList.stream()
                .filter(p->p.getSongAlbum().contains(albumName))
                .collect(Collectors.toList())
                .forEach(s->{list.add(s);System.out.printf("%-10s %-30s %-20s %-15s %-20s %-15s\n", s.getSongID(),s.getSongName(),s.getSongArtist(),s.getSongGenre(),s.getSongAlbum(),s.getSongDuration());});

        return list.size();
    }


}
