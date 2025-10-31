*** Settings ***
Library         RequestsLibrary
Library         Collections
Library         DateTime
Library         FakerLibrary

*** Variables ***
# --- CONFIGURAÇÕES DE AMBIENTE (EXISTENTES) ---
${BASE_URL}    %{BASE_URL}
${ENDPOINT_EVENT}       /event
${ENDPOINT_TICKET}      /event/{eventId}/ticket

# --- DADOS FIXOS DO EVENTO (EXISTENTES) ---
${FUTURE_DATE_INITIAL}  2030-10-10T18:00:00
${FUTURE_DATE_FINAL}    2030-10-10T21:00:00
${SUCCESS_LOCATION}     Sala Alpha
${SUCCESS_CAPACITY}     100

# --- VARIÁVEIS DE CONTROLE GLOBAL (EXISTENTES) ---
${CREATED_EVENT_ID}     ${EMPTY}

# --- NOVAS VARIÁVEIS PARA TICKETS ---
${GLOBAL_EVENT_ID}       ${EMPTY}
${GLOBAL_TICKET_NAME_1}  ${EMPTY}
${GLOBAL_TICKET_NAME_2}  ${EMPTY}

# --- TAGS (EXISTENTES) ---
${TAG_ALL_TESTS}        POST_EVENT
${TAG_TICKET_TESTS}     TICKET_CRUD

# --- STATUS CODES (EXISTENTES) ---
${STATUS_201}           201
${STATUS_400}           400
${STATUS_404}           404
${STATUS_409}           409
${STATUS_422}           422