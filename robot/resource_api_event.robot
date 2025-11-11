*** Settings ***
Library         RequestsLibrary
Library         Collections
Library         DateTime
Library         FakerLibrary

*** Variables ***
# --- CONFIGURAÇÕES DE AMBIENTE (EXISTENTES) ---
${BASE_URL}      %{BASE_URL}
${public_key}    %{MERCADO_PAGO_PUBLIC_KEY}
# ${BASE_URL}             http://localhost:8080/api
${ENDPOINT_EVENT}       /event
${ENDPOINT_TICKET}      /event/{eventId}/ticket-type
${ENDPOINT_BUY_TICKET}  /ticket/buy
${ENDPOINT_UPDATE_TICKET}    /ticket-type/{ticketTypeId}
${ENDPOINT_DELETE_TICKET}    /ticket-type/{ticketTypeId}

# --- DADOS FIXOS DO EVENTO (EXISTENTES) ---
${FUTURE_DATE_INITIAL}  2030-10-10T18:00:00
${FUTURE_DATE_FINAL}    2030-10-10T21:00:00
${SUCCESS_LOCATION}     Sala Alpha
${SUCCESS_CAPACITY}     100

# --- VARIÁVEIS PARA TESTES DE ATUALIZAÇÃO ---

${UPDATED_TICKET_NAME}        Ingresso Atualizado
${UPDATED_TICKET_PRICE}       200
${UPDATED_TICKET_CAPACITY}    80
${UPDATED_DATE_INITIAL}       2030-10-10T10:00:00
${UPDATED_DATE_FINAL}         2030-10-10T12:00:00

# --- VARIÁVEIS DE CONTROLE GLOBAL (EXISTENTES) ---
${CREATED_EVENT_ID}     ${EMPTY}

# --- NOVAS VARIÁVEIS PARA TICKETS ---
${GLOBAL_EVENT_ID}       ${EMPTY}
${GLOBAL_TICKET_NAME_1}  ${EMPTY}
${GLOBAL_TICKET_NAME_2}  ${EMPTY}

${uuid_inexistente}          f2f1e0d9-c8b7-4a65-83e2-1d5c7b9a4f3e
${uuid_invalido}             1234

# --- TAGS (EXISTENTES) ---
${TAG_ALL_TESTS}        POST_EVENT
${TAG_TICKET_TESTS}     TICKET_CRUD

# --- STATUS CODES (EXISTENTES) ---
${STATUS_201}           201
${STATUS_204}           204
${STATUS_400}           400
${STATUS_404}           404
${STATUS_409}           409
${STATUS_422}           422