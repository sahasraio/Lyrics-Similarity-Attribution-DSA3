/* =====================================================
   LYRICMATCH - MAIN JAVASCRIPT
   ===================================================== */


/* =====================================================
   GET HTML ELEMENTS
   ===================================================== */

const analyzeButton =
    document.getElementById("analyzeButton");

const clearButton =
    document.getElementById("clearButton");

const lyricsInput =
    document.getElementById("lyricsInput");

const resultsContainer =
    document.getElementById("resultsContainer");


/* =====================================================
   BACKEND URL
   ===================================================== */

const API_URL =
    "http://localhost:8080/api/analyze";


/* =====================================================
   ANALYZE LYRICS
   ===================================================== */

analyzeButton.addEventListener(
    "click",
    async function () {

        const lyrics =
            lyricsInput.value.trim();


        /* ---------------------------------------------
           CHECK EMPTY INPUT
           --------------------------------------------- */

        if (lyrics === "") {

            alert(
                "Please enter some lyrics first."
            );

            lyricsInput.focus();

            return;
        }


        /* ---------------------------------------------
           SHOW LOADING MESSAGE
           --------------------------------------------- */

        resultsContainer.innerHTML = `

            <div class="empty-result">

                <div class="empty-icon">
                    ⏳
                </div>

                <h3>
                    Analyzing Lyrics...
                </h3>

                <p>
                    Please wait while the DSA algorithms
                    process your lyrics.
                </p>

            </div>

        `;


        /* Disable button while processing */

        analyzeButton.disabled = true;

        analyzeButton.textContent =
            "Analyzing...";


        try {

            /* -----------------------------------------
               SEND REQUEST TO JAVA BACKEND
               ----------------------------------------- */

            const response =
                await fetch(
                    API_URL,
                    {
                        method: "POST",

                        headers: {
                            "Content-Type":
                                "application/json"
                        },

                        body: JSON.stringify({
                            lyrics: lyrics
                        })
                    }
                );


            /* -----------------------------------------
               READ RESPONSE
               ----------------------------------------- */

            const data =
                await response.json();


            /* -----------------------------------------
               CHECK SERVER ERROR
               ----------------------------------------- */

            if (!response.ok) {

                throw new Error(
                    data.error ||
                    "Something went wrong."
                );
            }


            /* -----------------------------------------
               DISPLAY RESULTS
               ----------------------------------------- */

            displayResults(data);


        } catch (error) {

            console.error(
                "Analysis Error:",
                error
            );


            resultsContainer.innerHTML = `

                <div class="empty-result">

                    <div class="empty-icon">
                        !
                    </div>

                    <h3>
                        Connection Error
                    </h3>

                    <p>
                        Could not connect to the Java
                        backend.
                        Make sure BackendServer is running.
                    </p>

                </div>

            `;

        } finally {

            /* Enable button again */

            analyzeButton.disabled = false;

            analyzeButton.textContent =
                "Analyze Lyrics";
        }

    }
);


/* =====================================================
   DISPLAY RESULTS
   ===================================================== */

function displayResults(data) {

    /* ---------------------------------------------
       GET RESULT INFORMATION
       --------------------------------------------- */

    const results =
        data.results || [];

    const passages =
        data.passages || [];


    const totalSongs =
        results.length;

    const totalPassages =
        passages.length;


    const topCandidate =
        data.topCandidate || "No Match";


    const topScore =
        Number(data.topScore || 0).toFixed(2);


    /* ---------------------------------------------
       START HTML
       --------------------------------------------- */

    let html = `

        <div class="analysis-result">

            <h2>
                Similarity Analysis
            </h2>


            <!-- DASHBOARD STATISTICS -->

            <div class="stats-grid">


                <div class="stat-card">

                    <span class="stat-label">
                        TOP MATCH
                    </span>

                    <span class="stat-value">
                        ${escapeHTML(topCandidate)}
                    </span>

                </div>


                <div class="stat-card">

                    <span class="stat-label">
                        BEST SCORE
                    </span>

                    <span class="stat-value">
                        ${topScore}%
                    </span>

                </div>


                <div class="stat-card">

                    <span class="stat-label">
                        SONGS ANALYZED
                    </span>

                    <span class="stat-value">
                        ${totalSongs}
                    </span>

                </div>


                <div class="stat-card">

                    <span class="stat-label">
                        PASSAGES FOUND
                    </span>

                    <span class="stat-value">
                        ${totalPassages}
                    </span>

                </div>

            </div>


            <!-- TOP MATCH -->

            <div class="top-result">

                <h3>
                    Top Matching Song
                </h3>

                <p class="top-song">
                    ${escapeHTML(topCandidate)}
                </p>

                <p>
                    Similarity Score:
                    <strong>
                        ${topScore}%
                    </strong>
                </p>

            </div>


            <!-- SONG RANKING -->

            <h3>
                Song Similarity Ranking
            </h3>


            <div class="result-list">

    `;


    /* ---------------------------------------------
       DISPLAY SONG RESULTS
       --------------------------------------------- */

    if (results.length === 0) {

        html += `

            <div class="passage-box">

                <p>
                    No songs were found for analysis.
                </p>

            </div>

        `;

    } else {

        results.forEach(
            function (result, index) {

                const score =
                    Math.max(
                        0,
                        Math.min(
                            100,
                            Number(result.score || 0)
                        )
                    );


                const formattedScore =
                    score.toFixed(2);


                html += `

                    <div class="result-card">

                        <div class="result-info">


                            <div class="song-title">

                                <span class="rank">
                                    #${index + 1}
                                </span>

                                <strong>
                                    ${escapeHTML(
                                        result.song
                                    )}
                                </strong>

                            </div>


                            <div class="score-bar">

                                <div
                                    class="score-fill"
                                    style="width: ${score}%"
                                >
                                </div>

                            </div>


                        </div>


                        <span class="score">
                            ${formattedScore}%
                        </span>


                    </div>

                `;

            }
        );

    }


    html += `

            </div>


            <!-- MATCHING PASSAGES -->

            <h3>
                Matching Passages
            </h3>

    `;


    /* ---------------------------------------------
       DISPLAY PASSAGES
       --------------------------------------------- */

    if (passages.length === 0) {

        html += `

            <div class="passage-box">

                <p>
                    No matching passages found.
                </p>

            </div>

        `;

    } else {

        passages.forEach(
            function (item) {

                html += `

                    <div class="passage-box">

                        <strong>
                            ${escapeHTML(
                                item.song
                            )}
                        </strong>

                        <p>
                            "${escapeHTML(
                                item.passage
                            )}"
                        </p>

                    </div>

                `;

            }
        );

    }


    /* ---------------------------------------------
       ATTRIBUTION NOTE
       --------------------------------------------- */

    html += `

            <div class="notice-box">

                <h3>
                    Attribution Note
                </h3>

                <p>
                    The similarity score and matching
                    passages indicate textual similarity
                    between the entered lyrics and the
                    stored songs. Similarity alone does
                    not prove authorship or plagiarism.
                </p>

            </div>


        </div>

    `;


    /* ---------------------------------------------
       PUT RESULTS ON PAGE
       --------------------------------------------- */

    resultsContainer.innerHTML =
        html;


    /* ---------------------------------------------
       SMOOTHLY SCROLL TO RESULTS
       --------------------------------------------- */

    resultsContainer.scrollIntoView({
        behavior: "smooth",
        block: "start"
    });

}


/* =====================================================
   CLEAR BUTTON
   ===================================================== */

clearButton.addEventListener(
    "click",
    function () {

        /* Clear textarea */

        lyricsInput.value = "";


        /* Restore default result screen */

        resultsContainer.innerHTML = `

            <div class="empty-result">

                <div class="empty-icon">
                    ♪
                </div>

                <h3>
                    No Analysis Yet
                </h3>

                <p>
                    Enter lyrics above and click
                    "Analyze Lyrics" to see results.
                </p>

            </div>

        `;


        lyricsInput.focus();

    }
);


/* =====================================================
   HTML ESCAPE FUNCTION
   ===================================================== */

function escapeHTML(value) {

    if (value === null ||
        value === undefined) {

        return "";

    }


    return String(value)
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}


/* =====================================================
   ENTER KEY SUPPORT
   ===================================================== */

lyricsInput.addEventListener(
    "keydown",
    function (event) {

        /*
         * Ctrl + Enter or
         * Shift + Enter
         * can start analysis.
         */

        if (
            event.ctrlKey &&
            event.key === "Enter"
        ) {

            analyzeButton.click();

        }

    }
);


/* =====================================================
   PAGE READY MESSAGE
   ===================================================== */

console.log(
    "LyricMatch frontend loaded successfully."
);

console.log(
    "Backend API:",
    API_URL
);