package org.example;

import org.apache.jena.rdf.model.*;
import java.io.FileOutputStream;
import org.apache.jena.query.*;
import org.apache.jena.reasoner.*;
import org.apache.jena.reasoner.rulesys.*;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

public class ResearchGraphWithReasoning {
    public static void main(String[] args) {
        // setting up namespace - basically our unique identifier
        String ns = "http://example.org/ps2games#";

        // Step 1: Create base RDF model
        // this is where all our data will live
        Model baseModel = ModelFactory.createDefaultModel();

        // Namespaced properties - these are like the "verbs" in our sentences
        // so we can say "game X has title Y" or "developer Z developed game A"
        Property hasTitle = baseModel.createProperty(ns + "hasTitle");
        Property hasName = baseModel.createProperty(ns + "hasName");
        Property developedBy = baseModel.createProperty(ns + "developedBy"); // who made the game
        Property publishedBy = baseModel.createProperty(ns + "publishedBy"); // who released it
        Property hasGenre = baseModel.createProperty(ns + "hasGenre");
        Property releasedIn = baseModel.createProperty(ns + "releasedIn"); // year it came out
        Property hasRating = baseModel.createProperty(ns + "hasRating"); // user rating
        Property hasSalesCount = baseModel.createProperty(ns + "hasSalesCount"); // how many copies sold
        Property hasMetacriticScore = baseModel.createProperty(ns + "hasMetacriticScore"); // critic scores
        Property partOfSeries = baseModel.createProperty(ns + "partOfSeries"); // like FF series
        Property hasProtagonist = baseModel.createProperty(ns + "hasProtagonist"); // main character
        Property hasAntagonist = baseModel.createProperty(ns + "hasAntagonist"); // bad guy
        Property hasLocation = baseModel.createProperty(ns + "hasLocation"); // where game takes place
        Property supportsFeature = baseModel.createProperty(ns + "supportsFeature");
        Property hasAgeRating = baseModel.createProperty(ns + "hasAgeRating"); // T for teen, M for mature etc
        Property wonAward = baseModel.createProperty(ns + "wonAward");
        Property isSequelOf = baseModel.createProperty(ns + "isSequelOf"); // god of war 2 is sequel of god of war
        Property hasGameplayHours = baseModel.createProperty(ns + "hasGameplayHours"); // how long to beat
        Property hasMultiplayer = baseModel.createProperty(ns + "hasMultiplayer"); // yes/no
        Property foundedIn = baseModel.createProperty(ns + "foundedIn"); // when company started
        Property headquarteredIn = baseModel.createProperty(ns + "headquarteredIn"); // where company is based

        // Step 2: Create Ontology Classes
        // these are like categories or types of things
        Resource Game = baseModel.createResource(ns + "Game"); // parent class
        Resource RPG = baseModel.createResource(ns + "RPG"); // role playing games
        Resource ActionGame = baseModel.createResource(ns + "ActionGame"); // action games
        Resource PlatformGame = baseModel.createResource(ns + "PlatformGame"); // jumping games
        Resource FightingGame = baseModel.createResource(ns + "FightingGame"); // tekken, street fighter
        Resource RacingGame = baseModel.createResource(ns + "RacingGame"); // need for speed etc
        Resource SportGame = baseModel.createResource(ns + "SportGame"); // fifa, madden
        Resource Developer = baseModel.createResource(ns + "Developer"); // companies that make games
        Resource Publisher = baseModel.createResource(ns + "Publisher"); // companies that sell games
        Resource Character = baseModel.createResource(ns + "Character"); // people in games
        Resource Protagonist = baseModel.createResource(ns + "Protagonist"); // good guys
        Resource Antagonist = baseModel.createResource(ns + "Antagonist"); // bad guys
        Resource GameSeries = baseModel.createResource(ns + "GameSeries"); // final fantasy, gta series
        Resource Award = baseModel.createResource(ns + "Award"); // game of the year etc
        Resource Genre = baseModel.createResource(ns + "Genre"); // action, rpg, racing

        // Declare hierarchical relationships
        // basically saying "RPG is a type of Game"
        baseModel.add(RPG, RDFS.subClassOf, Game);
        baseModel.add(ActionGame, RDFS.subClassOf, Game);
        baseModel.add(PlatformGame, RDFS.subClassOf, Game);
        baseModel.add(FightingGame, RDFS.subClassOf, Game);
        baseModel.add(RacingGame, RDFS.subClassOf, Game);
        baseModel.add(SportGame, RDFS.subClassOf, Game);
        baseModel.add(Protagonist, RDFS.subClassOf, Character); // protagonist is type of character
        baseModel.add(Antagonist, RDFS.subClassOf, Character); // antagonist is type of character

        // Step 3: Create Genres
        // making specific genre instances
        Resource rpgGenre = baseModel.createResource(ns + "RPGGenre")
                .addProperty(RDF.type, Genre)
                .addProperty(hasName, "Role-Playing Game");

        Resource actionGenre = baseModel.createResource(ns + "ActionGenre")
                .addProperty(RDF.type, Genre)
                .addProperty(hasName, "Action");

        Resource platformGenre = baseModel.createResource(ns + "PlatformGenre")
                .addProperty(RDF.type, Genre)
                .addProperty(hasName, "Platform");

        Resource fightingGenre = baseModel.createResource(ns + "FightingGenre")
                .addProperty(RDF.type, Genre)
                .addProperty(hasName, "Fighting");

        Resource racingGenre = baseModel.createResource(ns + "RacingGenre")
                .addProperty(RDF.type, Genre)
                .addProperty(hasName, "Racing");

        // Step 4: Create Game Series
        // these are the major franchises
        Resource finalFantasySeries = baseModel.createResource(ns + "FinalFantasySeries")
                .addProperty(RDF.type, GameSeries)
                .addProperty(hasName, "Final Fantasy");

        Resource grandTheftAutoSeries = baseModel.createResource(ns + "GrandTheftAutoSeries")
                .addProperty(RDF.type, GameSeries)
                .addProperty(hasName, "Grand Theft Auto");

        Resource tekkenSeries = baseModel.createResource(ns + "TekkenSeries")
                .addProperty(RDF.type, GameSeries)
                .addProperty(hasName, "Tekken");

        Resource metalGearSeries = baseModel.createResource(ns + "MetalGearSeries")
                .addProperty(RDF.type, GameSeries)
                .addProperty(hasName, "Metal Gear");

        // Step 5: Create Developers
        // the companies that actually make the games
        Resource squareEnix = baseModel.createResource(ns + "SquareEnix")
                .addProperty(RDF.type, Developer)
                .addProperty(hasName, "Square Enix") // japanese company, makes FF
                .addProperty(foundedIn, "1986")
                .addProperty(headquarteredIn, "Tokyo, Japan");

        Resource rockstarGames = baseModel.createResource(ns + "RockstarGames")
                .addProperty(RDF.type, Developer)
                .addProperty(hasName, "Rockstar Games") // american company, makes GTA
                .addProperty(foundedIn, "1998")
                .addProperty(headquarteredIn, "New York, USA");

        Resource namco = baseModel.createResource(ns + "Namco")
                .addProperty(RDF.type, Developer)
                .addProperty(hasName, "Namco") // old japanese company, makes tekken
                .addProperty(foundedIn, "1955")
                .addProperty(headquarteredIn, "Tokyo, Japan");

        Resource konami = baseModel.createResource(ns + "Konami")
                .addProperty(RDF.type, Developer)
                .addProperty(hasName, "Konami") // another japanese company
                .addProperty(foundedIn, "1969")
                .addProperty(headquarteredIn, "Tokyo, Japan");

        Resource santaMonicaStudio = baseModel.createResource(ns + "SantaMonicaStudio")
                .addProperty(RDF.type, Developer)
                .addProperty(hasName, "Santa Monica Studio") // sony's studio, makes god of war
                .addProperty(foundedIn, "1999")
                .addProperty(headquarteredIn, "Los Angeles, USA");

        // Step 6: Create Publishers
        // companies that distribute and sell games
        Resource sony = baseModel.createResource(ns + "Sony")
                .addProperty(RDF.type, Publisher)
                .addProperty(hasName, "Sony Computer Entertainment"); // playstation maker

        Resource take2 = baseModel.createResource(ns + "Take2")
                .addProperty(RDF.type, Publisher)
                .addProperty(hasName, "Take-Two Interactive"); // publishes GTA

        // Step 7: Create Characters
        // main characters from the games
        Resource cloud = baseModel.createResource(ns + "Cloud")
                .addProperty(RDF.type, Protagonist)
                .addProperty(hasName, "Cloud Strife"); // FF7 main character

        Resource sephiroth = baseModel.createResource(ns + "Sephiroth")
                .addProperty(RDF.type, Antagonist)
                .addProperty(hasName, "Sephiroth"); // FF7 bad guy

        Resource cj = baseModel.createResource(ns + "CJ")
                .addProperty(RDF.type, Protagonist)
                .addProperty(hasName, "Carl Johnson"); // GTA SA main character

        Resource jin = baseModel.createResource(ns + "Jin")
                .addProperty(RDF.type, Protagonist)
                .addProperty(hasName, "Jin Kazama"); // tekken character

        Resource solidSnake = baseModel.createResource(ns + "SolidSnake")
                .addProperty(RDF.type, Protagonist)
                .addProperty(hasName, "Solid Snake"); // metal gear main guy

        Resource kratos = baseModel.createResource(ns + "Kratos")
                .addProperty(RDF.type, Protagonist)
                .addProperty(hasName, "Kratos"); // god of war angry guy

        // Step 8: Create Awards
        // different types of awards games can win
        Resource gameOfYear = baseModel.createResource(ns + "GameOfYear")
                .addProperty(RDF.type, Award)
                .addProperty(hasName, "Game of the Year");

        Resource bestRPG = baseModel.createResource(ns + "BestRPG")
                .addProperty(RDF.type, Award)
                .addProperty(hasName, "Best RPG");

        // Step 9: Create PS2 Games with complex relationships
        // now the actual games with all their details
        Resource ff10 = baseModel.createResource(ns + "FinalFantasyX")
                .addProperty(RDF.type, RPG) // it's an RPG
                .addProperty(hasTitle, "Final Fantasy X")
                .addProperty(developedBy, squareEnix) // square made it
                .addProperty(publishedBy, sony) // sony published it
                .addProperty(hasGenre, rpgGenre)
                .addProperty(releasedIn, "2001") // came out in 2001
                .addProperty(hasRating, "9.0") // really good rating
                .addProperty(hasSalesCount, "8500000") // sold 8.5 million
                .addProperty(hasMetacriticScore, "92") // critics liked it
                .addProperty(partOfSeries, finalFantasySeries)
                .addProperty(hasProtagonist, cloud) // cloud is main character
                .addProperty(hasAntagonist, sephiroth) // sephiroth is bad guy
                .addProperty(hasLocation, "Spira") // takes place in spira
                .addProperty(hasAgeRating, "T") // teen rated
                .addProperty(wonAward, bestRPG) // won best rpg
                .addProperty(hasGameplayHours, "60") // takes 60 hours to beat
                .addProperty(hasMultiplayer, "No"); // single player only

        Resource gtaSanAndreas = baseModel.createResource(ns + "GTASanAndreas")
                .addProperty(RDF.type, ActionGame) // it's action game
                .addProperty(hasTitle, "Grand Theft Auto: San Andreas")
                .addProperty(developedBy, rockstarGames) // rockstar made it
                .addProperty(publishedBy, take2) // take2 published
                .addProperty(hasGenre, actionGenre)
                .addProperty(releasedIn, "2004") // 2004 release
                .addProperty(hasRating, "9.6") // amazing rating
                .addProperty(hasSalesCount, "17330000") // best seller, 17+ million
                .addProperty(hasMetacriticScore, "95") // critics loved it
                .addProperty(partOfSeries, grandTheftAutoSeries)
                .addProperty(hasProtagonist, cj) // CJ is main character
                .addProperty(hasLocation, "San Andreas") // california based
                .addProperty(hasAgeRating, "M") // mature rated
                .addProperty(wonAward, gameOfYear) // won game of year
                .addProperty(hasGameplayHours, "100") // huge game, 100 hours
                .addProperty(hasMultiplayer, "Yes"); // has multiplayer

        Resource tekken3 = baseModel.createResource(ns + "Tekken3")
                .addProperty(RDF.type, FightingGame) // fighting game
                .addProperty(hasTitle, "Tekken 3")
                .addProperty(developedBy, namco) // namco made and published
                .addProperty(publishedBy, namco)
                .addProperty(hasGenre, fightingGenre)
                .addProperty(releasedIn, "1998") // old game, 1998
                .addProperty(hasRating, "9.5") // excellent rating
                .addProperty(hasSalesCount, "8360000") // sold well
                .addProperty(hasMetacriticScore, "96") // critics loved it
                .addProperty(partOfSeries, tekkenSeries)
                .addProperty(hasProtagonist, jin) // jin is main fighter
                .addProperty(hasLocation, "Various") // fights in different places
                .addProperty(hasAgeRating, "T") // teen rated
                .addProperty(hasGameplayHours, "15") // not too long
                .addProperty(hasMultiplayer, "Yes"); // fighting games need multiplayer

        Resource mgs2 = baseModel.createResource(ns + "MetalGearSolid2")
                .addProperty(RDF.type, ActionGame) // action/stealth game
                .addProperty(hasTitle, "Metal Gear Solid 2: Sons of Liberty")
                .addProperty(developedBy, konami) // konami made it
                .addProperty(publishedBy, konami)
                .addProperty(hasGenre, actionGenre)
                .addProperty(releasedIn, "2001") // 2001 release
                .addProperty(hasRating, "9.6") // amazing game
                .addProperty(hasSalesCount, "7000000") // sold 7 million
                .addProperty(hasMetacriticScore, "96") // perfect scores
                .addProperty(partOfSeries, metalGearSeries)
                .addProperty(hasProtagonist, solidSnake) // snake is main character
                .addProperty(hasLocation, "Big Shell") // takes place on oil rig
                .addProperty(hasAgeRating, "M") // mature content
                .addProperty(hasGameplayHours, "12") // decent length
                .addProperty(hasMultiplayer, "No"); // single player story

        Resource godOfWar = baseModel.createResource(ns + "GodOfWar")
                .addProperty(RDF.type, ActionGame) // action adventure
                .addProperty(hasTitle, "God of War")
                .addProperty(developedBy, santaMonicaStudio) // santa monica made it
                .addProperty(publishedBy, sony) // sony published
                .addProperty(hasGenre, actionGenre)
                .addProperty(releasedIn, "2005") // 2005 release
                .addProperty(hasRating, "9.0") // great game
                .addProperty(hasSalesCount, "4600000") // sold well
                .addProperty(hasMetacriticScore, "94") // critics loved it
                .addProperty(hasProtagonist, kratos) // kratos is angry main character
                .addProperty(hasLocation, "Ancient Greece") // greek mythology setting
                .addProperty(hasAgeRating, "M") // violent content
                .addProperty(hasGameplayHours, "8") // short but sweet
                .addProperty(hasMultiplayer, "No"); // single player

        Resource godOfWar2 = baseModel.createResource(ns + "GodOfWar2")
                .addProperty(RDF.type, ActionGame) // sequel to god of war
                .addProperty(hasTitle, "God of War II")
                .addProperty(developedBy, santaMonicaStudio) // same studio
                .addProperty(publishedBy, sony) // sony again
                .addProperty(hasGenre, actionGenre)
                .addProperty(releasedIn, "2007") // 2007 release
                .addProperty(hasRating, "9.2") // even better than first
                .addProperty(hasSalesCount, "4240000") // similar sales
                .addProperty(hasMetacriticScore, "93") // still great scores
                .addProperty(hasProtagonist, kratos) // kratos again
                .addProperty(hasLocation, "Ancient Greece") // same setting
                .addProperty(hasAgeRating, "M") // still violent
                .addProperty(isSequelOf, godOfWar) // this is important - it's sequel
                .addProperty(hasGameplayHours, "10") // bit longer
                .addProperty(hasMultiplayer, "No"); // single player

        // Step 10: Print Base Model
        System.out.println("==== PS2 Games Database RDF Graph (Turtle) ====");
        baseModel.write(System.out, "TURTLE"); // outputs in turtle format
        
        // Save to file - so we can look at it later
        try (FileOutputStream out = new FileOutputStream("ps2_games_database.ttl")) {
            baseModel.write(out, "TURTLE");
            System.out.println("\nGraph saved to ps2_games_database.ttl");
        } catch (Exception e) {
            System.err.println("Error saving to file: " + e.getMessage());
        }

        // Step 11: Apply Reasoning
        // this is where it gets smart - reasoner can infer new facts
        Reasoner reasoner = ReasonerRegistry.getRDFSReasoner();
        InfModel infModel = ModelFactory.createInfModel(reasoner, baseModel);

        // Step 12: Run Complex SPARQL Queries
        // now we can ask complex questions about our data
        
        // Query 1: Find all games (inferred from subclasses)
        // this will find RPGs, Action games etc because reasoner knows they're Games
        String query1 = 
            "PREFIX ps2: <http://example.org/ps2games#> " +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
            "SELECT ?title ?type ?rating WHERE { " +
            "  ?game a ps2:Game . " + // find things that are Games
            "  ?game ps2:hasTitle ?title . " +
            "  ?game a ?type . " + // what specific type
            "  ?game ps2:hasRating ?rating . " +
            "  FILTER(?type != ps2:Game) " + // don't show generic Game type
            "} ORDER BY DESC(xsd:double(?rating))"; // order by rating, highest first
        
        System.out.println("\n==== Query 1: All PS2 Games by Rating ====");
        runQuery(query1, infModel);

        // Query 2: Find best-selling games
        // only games that sold more than 5 million copies
        String query2 = 
            "PREFIX ps2: <http://example.org/ps2games#> " +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
            "SELECT ?title ?sales ?developer WHERE { " +
            "  ?game a ps2:Game . " +
            "  ?game ps2:hasTitle ?title . " +
            "  ?game ps2:hasSalesCount ?sales . " +
            "  ?game ps2:developedBy ?dev . " +
            "  ?dev ps2:hasName ?developer . " +
            "  FILTER(xsd:int(?sales) > 5000000) " + // more than 5 million
            "} ORDER BY DESC(xsd:int(?sales))"; // highest sales first
        
        System.out.println("\n==== Query 2: Best-Selling PS2 Games (>5M sales) ====");
        runQuery(query2, infModel);

        // Query 3: Find Japanese developers and their games
        // interesting cultural analysis
        String query3 = 
            "PREFIX ps2: <http://example.org/ps2games#> " +
            "SELECT ?developer ?title ?year WHERE { " +
            "  ?dev a ps2:Developer . " +
            "  ?dev ps2:hasName ?developer . " +
            "  ?dev ps2:headquarteredIn ?location . " +
            "  ?game ps2:developedBy ?dev . " +
            "  ?game ps2:hasTitle ?title . " +
            "  ?game ps2:releasedIn ?year . " +
            "  FILTER(CONTAINS(?location, 'Japan')) " + // only japanese companies
            "} ORDER BY ?developer ?year";
        
        System.out.println("\n==== Query 3: Japanese Developers and Their Games ====");
        runQuery(query3, infModel);

        // Query 4: Find action games with protagonists
        // looking at character relationships
        String query4 = 
            "PREFIX ps2: <http://example.org/ps2games#> " +
            "SELECT ?title ?protagonist ?location WHERE { " +
            "  ?game a ps2:ActionGame . " + // only action games
            "  ?game ps2:hasTitle ?title . " +
            "  ?game ps2:hasProtagonist ?protag . " +
            "  ?protag ps2:hasName ?protagonist . " +
            "  ?game ps2:hasLocation ?location . " +
            "} ORDER BY ?title";
        
        System.out.println("\n==== Query 4: Action Games with Protagonists ====");
        runQuery(query4, infModel);

        // Query 5: Find game series with multiple games
        // using aggregation to count games per series
        String query5 = 
            "PREFIX ps2: <http://example.org/ps2games#> " +
            "SELECT ?seriesName (COUNT(?game) AS ?gameCount) WHERE { " +
            "  ?series a ps2:GameSeries . " +
            "  ?series ps2:hasName ?seriesName . " +
            "  ?game ps2:partOfSeries ?series . " +
            "} GROUP BY ?seriesName " + // group by series
            "HAVING (COUNT(?game) > 0) " + // only series with games
            "ORDER BY DESC(?gameCount)"; // most games first
        
        System.out.println("\n==== Query 5: Game Series with Game Counts ====");
        runQuery(query5, infModel);

        // Query 6: Find games with high Metacritic scores
        // critic favorites
        String query6 = 
            "PREFIX ps2: <http://example.org/ps2games#> " +
            "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> " +
            "SELECT ?title ?score ?year ?genre WHERE { " +
            "  ?game a ps2:Game . " +
            "  ?game ps2:hasTitle ?title . " +
            "  ?game ps2:hasMetacriticScore ?score . " +
            "  ?game ps2:releasedIn ?year . " +
            "  ?game ps2:hasGenre ?genreRes . " +
            "  ?genreRes ps2:hasName ?genre . " +
            "  FILTER(xsd:int(?score) >= 90) " + // 90+ metacritic score
            "} ORDER BY DESC(xsd:int(?score))"; // highest scores first
        
        System.out.println("\n==== Query 6: High-Scoring Games (Metacritic >= 90) ====");
        runQuery(query6, infModel);

        // Query 7: Find sequels and their predecessors
        // sequel relationships
        String query7 = 
            "PREFIX ps2: <http://example.org/ps2games#> " +
            "SELECT ?sequelTitle ?originalTitle WHERE { " +
            "  ?sequel ps2:isSequelOf ?original . " + // sequel relationship
            "  ?sequel ps2:hasTitle ?sequelTitle . " +
            "  ?original ps2:hasTitle ?originalTitle . " +
            "} ORDER BY ?originalTitle";
        
        System.out.println("\n==== Query 7: Game Sequels and Their Predecessors ====");
        runQuery(query7, infModel);

        // Query 8: Find award-winning games
        // games that won awards
        String query8 = 
            "PREFIX ps2: <http://example.org/ps2games#> " +
            "SELECT ?title ?awardName ?developer WHERE { " +
            "  ?game ps2:wonAward ?award . " + // games with awards
            "  ?award ps2:hasName ?awardName . " +
            "  ?game ps2:hasTitle ?title . " +
            "  ?game ps2:developedBy ?dev . " +
            "  ?dev ps2:hasName ?developer . " +
            "} ORDER BY ?awardName";
        
        System.out.println("\n==== Query 8: Award-Winning Games ====");
        runQuery(query8, infModel);
    }

    // helper method to run queries
    private static void runQuery(String queryString, Model model) {
        Query query = QueryFactory.create(queryString);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            ResultSet results = qexec.execSelect();
            ResultSetFormatter.out(System.out, results, query); // prints results nicely
        }
    }
}