*** Settings ***
Library         RequestsLibrary
Library         Collections
Library         DateTime
Library         FakerLibrary
Library         String
Resource        resource_api_event.robot

*** Keywords ***
# --- SETUP E SESSÃO --- (EXISTENTE)
Criar Sessao API
    [Documentation]     Cria uma sessao HTTP persistente.
    Create Session      api_session     ${BASE_URL}     verify=True

# --- KEYWORDS EXISTENTES DO cases_post_event.robot (MANTIDAS) ---
Executar POST e Validar Status
    [Documentation]     Executa POST com payload e valida status code.
    [Arguments]         ${payload}      ${expected_status}
    
    ${headers}=         Create Dictionary       Content-Type=application/json
    ${response}=        POST On Session     api_session     ${ENDPOINT_EVENT}
    ...             json=${payload} 
    ...             headers=${headers}
    ...             expected_status=any

    Should Be Equal As Integers      ${response.status_code}     ${expected_status}
    RETURN      ${response}

Validar Response Sucesso 201
    [Arguments]         ${response}     ${expected_name}
    [Documentation]     Valida o conteudo da resposta 201.
    ${response_json}=   Set Variable    ${response.json()}
    Should Not Be Empty     ${response_json['id']}
    Should Not Be Empty     ${response_json['createdAt']}
    Should Not Be Empty     ${response_json['updatedAt']}
    
    # Validacoes de dados de sucesso
    Should Be Equal     ${response_json['name']}    ${expected_name}
    Should Be Equal As Integers      ${response_json['capacity']}     ${SUCCESS_CAPACITY}
    
Validar Mensagem de Erro
    [Arguments]         ${response}     ${partial_message}
    [Documentation]     Verifica se a resposta de erro contem a mensagem esperada.
    ${response_text}=   Set Variable    ${response.text}
    Should Contain      ${response_text}    ${partial_message}

Gerar Nome Evento Aleatorio
    [Documentation]     Gera um nome de evento unico e aleatorio
    ${random_word}=     FakerLibrary.Word
    ${timestamp}=       Get Current Date    result_format=%H%M%S
    ${event_name}=      Set Variable    Evento ${random_word} ${timestamp}
    RETURN      ${event_name}

Gerar Payload Sucesso Completo Aleatório
    [Documentation]     Gera um payload com todos os campos validos.
    ${event_name}=      Gerar Nome Evento Aleatorio
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             description=Descricao completa do evento.
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             ageRestriction=18
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Gerar Payload Sucesso Completo 
    [Documentation]     Gera um payload com todos os campos validos.
    &{payload}=     Create Dictionary
    ...             name=teste
    ...             description=Descricao completa do evento.
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             ageRestriction=18
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Gerar Payload Sucesso Minimo
    [Documentation]     Gera um payload apenas com campos obrigatorios validos.
    ${event_name}=      Gerar Nome Evento Aleatorio
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Gerar Payload Datas Iguais
    [Documentation]     Gera um payload onde data inicial e igual a final.
    ${event_name}=      Gerar Nome Evento Aleatorio
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_INITIAL}
    RETURN      ${payload}

Gerar Payload Campo Ausente
    [Arguments]         ${missing_field}
    [Documentation]     Remove um campo obrigatorio do payload de sucesso minimo.
    ${event_name}=      Gerar Nome Evento Aleatorio
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    Remove From Dictionary      ${payload}      ${missing_field}
    RETURN      ${payload}

Gerar Payload Campo Negativo
    [Arguments]         ${field_name}       ${field_value}
    [Documentation]     Define um valor invalido (negativo) para um campo numerico.
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      ${field_name}=${field_value}
    RETURN      ${payload}

Gerar Payload Data Passada
    [Documentation]     Define dateInitial como uma data passada.
    ${past_date}=       Convert Date    2020-01-01T10:00:00     result_format=%Y-%m-%dT%H:%M:%S
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      dateInitial=${past_date}
    RETURN      ${payload}

Gerar Payload Data Final Anterior
    [Documentation]     Define dateFinal anterior a dateInitial.
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      dateInitial=2030-10-10T12:00:00
    Set To Dictionary       ${payload}      dateFinal=2030-10-10T11:00:00
    RETURN      ${payload}

Gerar Payload Formato Data Invalido
    [Documentation]     Define um formato de data invalido.
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      dateInitial=10/10/2030 18:00
    RETURN      ${payload}

Gerar Payload Tipo Invalido
    [Arguments]         ${field_name}       ${invalid_value}
    [Documentation]     Define um tipo de dado invalido (ex: int em vez de string).
    &{payload}=     Gerar Payload Sucesso Minimo
    Set To Dictionary       ${payload}      ${field_name}=${invalid_value}
    RETURN      ${payload}

# --- NOVAS KEYWORDS PARA TICKETS (ADICIONADAS) ---
Criar Evento Base Para Tickets
    [Documentation]     Cria um evento específico para testes de ticket
    &{event_payload}=   Gerar Payload Evento Para Tickets
    ${headers}=         Create Dictionary    Content-Type=application/json
    
    ${response}=        POST On Session     api_session     ${ENDPOINT_EVENT}
    ...                 json=${event_payload}
    ...                 headers=${headers}
    ...                 expected_status=any
    
    Should Be Equal As Integers      ${response.status_code}     201
    ${event_id}=        Set Variable    ${response.json()['id']}
    Should Not Be Empty    ${event_id}
    RETURN              ${event_id}

Gerar Payload Evento Para Tickets
    [Documentation]     Gera payload específico para testes de ticket
    ${event_name}=      Gerar Nome Evento Para Tickets
    &{payload}=     Create Dictionary
    ...             name=${event_name}
    ...             location=${SUCCESS_LOCATION}
    ...             capacity=${SUCCESS_CAPACITY}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Gerar Nome Evento Para Tickets
    [Documentation]     Gera nome único para eventos de ticket
    ${random_word}=     FakerLibrary.Word
    ${timestamp}=       Get Current Date    result_format=%Y%m%d%H%M%S
    ${event_name}=      Set Variable    Evento-Ticket-${random_word}-${timestamp}
    RETURN              ${event_name}

Executar Post Ticket Para Evento
    [Arguments]    ${event_id}    ${payload}    ${expected_status}
    
    Should Not Be Equal    ${event_id}    ${None}
    Should Not Be Empty    ${event_id}
    
    ${url_final}=   Replace String    ${ENDPOINT_TICKET}  {eventId}  ${event_id}
    ${headers}=     Create Dictionary   Content-Type=application/json
    
    ${response}=    POST On Session     api_session     ${url_final}
    ...             json=${payload} 
    ...             headers=${headers}
    ...             expected_status=any
    
    Should Be Equal As Integers      ${response.status_code}     ${expected_status}
    RETURN      ${response}

Gerar Nome Ticket Aleatorio
    [Documentation]     Gera nome único para ticket
    ${random_word}=     FakerLibrary.Word
    ${timestamp}=       Get Current Date    result_format=%H%M%S
    ${ticket_name}=     Set Variable    Ticket-${random_word}-${timestamp}
    RETURN      ${ticket_name}

Criar Payload Ticket Basico
    [Arguments]    ${nome}    ${valor}    ${capacidade}
    [Documentation]    Gera payload básico para ticket
    &{payload}=     Create Dictionary
    ...             name=${nome}
    ...             description=Ingresso de Teste ${nome}
    ...             price=${valor}
    ...             capacity=${capacidade}
    ...             dateInitial=${FUTURE_DATE_INITIAL}
    ...             dateFinal=${FUTURE_DATE_FINAL}
    RETURN      ${payload}

Criar Payload Ticket Com Datas
    [Arguments]    ${nome}    ${valor}    ${capacidade}    ${data_inicial}    ${data_final}
    [Documentation]    Gera payload para ticket com datas customizadas
    &{payload}=     Create Dictionary
    ...             name=${nome}
    ...             description=Ingresso de Teste ${nome}
    ...             price=${valor}
    ...             capacity=${capacidade}
    ...             dateInitial=${data_inicial}
    ...             dateFinal=${data_final}
    RETURN      ${payload}

Validar Response Ticket Sucesso 201
    [Arguments]     ${response}     ${nome_esperado}    ${event_id_esperado}
    [Documentation]     Valida resposta 201 do Ticket
    ${response_json}=   Set Variable    ${response.json()}
    Should Not Be Empty     ${response_json['id']}
    Should Be Equal     ${response_json['name']}    ${nome_esperado}
    Should Be Equal     ${response_json['event']}    ${event_id_esperado}

Validar Mensagem de Erro Ticket
    [Arguments]         ${response}     ${mensagem_parcial}
    [Documentation]     Verifica se resposta de erro contém mensagem esperada
    ${response_text}=   Set Variable    ${response.text}
    Should Contain      ${response_text}    ${mensagem_parcial}