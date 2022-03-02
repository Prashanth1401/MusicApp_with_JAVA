import java.sql.Time;
public class Song1 {
    String songId;
    String artist;
    String genre;
    String album;
    String songName;
    Time duration;

    public Song1() {}

    public Song1(String songId,String artist,String genre,String album,String songName,Time duration)
    {
        this.songId=songId;
        this.artist=artist;
        this.genre=genre;
        this.album=album;
        this.songName=songName;
        this.duration=duration;
    }

    public String toString()
    {
        return	getSongId()+" "+getArtist()+" "+getGenre()+" "+getAlbum()+" "+getSongName()+" "+getDuration();
    }

    public void setSongId(String SongId)
    {
        this.songId=SongId;
    }

    public String getSongId()
    {
        return songId;
    }

    public void setArtist(String art)
    {
        this.artist=art;
    }

    public String getArtist()
    {
        return artist;
    }

    public void setGenre(String Genre)
    {
        this.genre=Genre;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setAlbum(String Album)
    {
        this.album=Album;
    }

    public String getAlbum()
    {
        return album;
    }

    public void setSongName(String songName)
    {
        this.songName=songName;
    }

    public String getSongName()
    {
        return songName;
    }

    public void setDuration(Time time)
    {
        this.duration=time;
    }

    public Time getDuration()
    {
        return duration;
    }
}