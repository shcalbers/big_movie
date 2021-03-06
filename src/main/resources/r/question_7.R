library(ggplot2);

# Include 'bmdb.R' - BEGIN
library(DBI);

executeQuery <- function (query) {
  connection <- DBI::dbConnect(odbc::odbc(),
                               driver = 'PostgreSQL ANSI(x64)',
                               server = '84.86.32.123',
                               port   =  5432,
                               uid    = 'bmdb',
                               pwd    = 'B1g_M0v13_D4t4b4s3_P455w0rd_F0r_5ch00l');

  result <- DBI::dbSendQuery(connection, query);
  data <- DBI::dbFetch(result);
  DBI::dbClearResult(result);

  DBI::dbDisconnect(connection);

  return(data);
};
# Include 'bmdb.R' - END

data <- executeQuery("
    SELECT
        title$genres.genre,
        AVG(title$rating.average_rating) AS average_rating
    FROM
        title INNER JOIN title$genres ON title.id = title$genres.title_id
        INNER JOIN title$rating ON title$genres.title_id = title$rating.title_id
    WHERE
        title.start_year BETWEEN 1990 AND date_part('year', CURRENT_DATE)
    GROUP BY
        title$genres.genre
    ORDER BY
         title$genres.genre DESC;
");

png(file='./plots/question_7.png', width = 480, height = 480);
ggplot(data, aes(x=genre, y=average_rating, fill=genre)) + geom_bar(stat='identity');
dev.off();
